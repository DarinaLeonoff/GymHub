const createGymButton = document.getElementById('create-gym-btn');
const modal = document.getElementById('gym-modal');
const closeButton = document.getElementById('close-modal-btn');
const cancelButton = document.getElementById('cancel-btn');
const gymForm = document.getElementById('gym-form');
const formMessage = document.getElementById('form-message');

function openModal() {
    modal.classList.remove('hidden');
}

function closeModal() {
    modal.classList.add('hidden');
    gymForm.reset();
    clearMessage();
}

function clearMessage() {
    formMessage.textContent = '';
    formMessage.className = 'form-message';
}

function showMessage(message, type) {
    formMessage.textContent = message;
    formMessage.className = `form-message ${type}`;
}

createGymButton.addEventListener('click', openModal);
closeButton.addEventListener('click', closeModal);
cancelButton.addEventListener('click', closeModal);

// Закрытие при клике вне окна
modal.addEventListener('click', (event) => {
    if (event.target === modal) {
        closeModal();
    }
});

// Отправка формы
gymForm.addEventListener('submit', async (event) => {
    event.preventDefault();
    clearMessage();

    const gymData = {
        name: gymForm.elements.name.value.trim(),
        description: gymForm.elements.description.value.trim(),
        address: gymForm.elements.address.value.trim(),
        tel: gymForm.elements.tel.value.trim(),
        email: gymForm.elements.email.value.trim()
    };

    try {
        const response = await fetch('/gym/profile', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(gymData)
        });

        const responseText = await response.text();

        if (!response.ok) {
            let errorMessage = responseText;

            try {
                const errorData = JSON.parse(responseText);
                errorMessage = errorData.message || errorData.error || responseText;
            } catch {
                // Ответ сервера не является JSON
            }

            throw new Error(errorMessage || `Ошибка сервера: ${response.status}`);
        }

        showMessage('Зал успешно создан!', 'success');
        gymForm.reset();

    } catch (error) {
        showMessage(error.message || 'Не удалось создать зал', 'error');
    }
});
