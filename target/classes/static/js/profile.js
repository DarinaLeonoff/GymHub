let currentGym;

const editModal = document.getElementById('edit-modal');
const editButton = document.getElementById('edit-gym-btn');
const editForm = document.getElementById('edit-gym-form');
const editMessage = document.getElementById('edit-message');
const loading = document.getElementById('loading');
const profile = document.getElementById('profile');
const errorMessage = document.getElementById('error-message');

async function loadGymProfile() {
    const params = new URLSearchParams(window.location.search);
    const gymId = params.get('id');

    if (!gymId || !/^\d+$/.test(gymId)) {
        showError('Некорректный или отсутствующий ID зала');
        return;
    }

    try {
        const response = await fetch(`/gym/profile/${gymId}`);

        if (!response.ok) {
            if (response.status === 404) {
                throw new Error('Зал не найден');
            }

            throw new Error(`Ошибка сервера: ${response.status}`);
        }

        const gym = await response.json();
        currentGym = gym;
        updateProfile(gym);

        loading.classList.add('hidden');
        profile.classList.remove('hidden');

        document.getElementById('gym-name').textContent = gym.name;
        document.getElementById('gym-id').textContent = `ID: ${gym.id}`;
        document.getElementById('gym-description').textContent =
            gym.description || 'Описание отсутствует';

        document.getElementById('gym-address').textContent =
            gym.address;

        document.getElementById('gym-tel').textContent =
            gym.tel;

        document.getElementById('gym-email').textContent =
            gym.email;

        loading.classList.add('hidden');
        profile.classList.remove('hidden');

    } catch (error) {
        showError(error.message);
    }
}

function showError(message) {
    loading.classList.add('hidden');
    errorMessage.textContent = message;
    errorMessage.classList.remove('hidden');
}

function openEditModal() {
    editForm.elements['edit-name'].value = currentGym.name || '';
    editForm.elements['edit-description'].value =
        currentGym.description || '';
    editForm.elements['edit-address'].value =
        currentGym.address || '';
    editForm.elements['edit-tel'].value = currentGym.tel || '';
    editForm.elements['edit-email'].value = currentGym.email || '';

    editMessage.textContent = '';
    editMessage.className = 'form-message';

    editModal.classList.remove('hidden');
}

function closeEditModal() {
    editModal.classList.add('hidden');
    editForm.reset();
}

function openEditModal() {
    editForm.elements['edit-name'].value = currentGym.name || '';
    editForm.elements['edit-description'].value =
        currentGym.description || '';
    editForm.elements['edit-address'].value =
        currentGym.address || '';
    editForm.elements['edit-tel'].value = currentGym.tel || '';
    editForm.elements['edit-email'].value = currentGym.email || '';

    editMessage.textContent = '';
    editMessage.className = 'form-message';

    editModal.classList.remove('hidden');
}

function closeEditModal() {
    editModal.classList.add('hidden');
    editForm.reset();
}

editButton.addEventListener('click', openEditModal);

document.getElementById('close-edit-modal')
    .addEventListener('click', closeEditModal);

document.getElementById('cancel-edit')
    .addEventListener('click', closeEditModal);

editForm.addEventListener('submit', async (event) => {
    event.preventDefault();

    editMessage.textContent = '';

    const gymData = {};

    const fields = ['name', 'description', 'address', 'tel', 'email'];

    fields.forEach(field => {
        const value = editForm.elements[`edit-${field}`].value.trim();

        // Пустые поля не отправляем
        if (value !== '') {
            gymData[field] = value;
        }
    });

    try {
        const response = await fetch(`/gym/profile/${currentGym.id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(gymData)
        });

        const responseText = await response.text();

        if (!response.ok) {
            let message = responseText;

            try {
                const errorData = JSON.parse(responseText);
                message = errorData.message || errorData.error || responseText;
            } catch {
                // Ответ не JSON
            }

            throw new Error(message || `Ошибка сервера: ${response.status}`);
        }

        currentGym = JSON.parse(responseText);

        updateProfile(currentGym);

        editMessage.textContent = 'Изменения сохранены!';
        editMessage.className = 'form-message success';

    } catch (error) {
        editMessage.textContent = error.message;
        editMessage.className = 'form-message error';
    }
});

function updateProfile(gym) {
    document.getElementById('gym-name').textContent = gym.name;
    document.getElementById('gym-id').textContent = `ID: ${gym.id}`;

    document.getElementById('gym-description').textContent =
        gym.description || 'Описание отсутствует';

    document.getElementById('gym-address').textContent =
        gym.address || '';

    document.getElementById('gym-tel').textContent =
        gym.tel || '';

    document.getElementById('gym-email').textContent =
        gym.email || '';
}

editButton.addEventListener('click', openEditModal);

document.getElementById('close-edit-modal')
    .addEventListener('click', closeEditModal);

document.getElementById('cancel-edit')
    .addEventListener('click', closeEditModal);

loadGymProfile();