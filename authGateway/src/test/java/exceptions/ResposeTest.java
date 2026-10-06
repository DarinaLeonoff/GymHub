package exceptions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.gymhub.exceptions.Response;

import static org.junit.jupiter.api.Assertions.*;

class ResponseTest {

    @Test
    @DisplayName("Конструктор должен корректно заполнять поля exception и message")
    void constructor_ShouldPopulateFieldsFromException() {

        String errorMessage = "User with this email already registered";
        RuntimeException exception = new RuntimeException(errorMessage);

        Response response = new Response(exception);

        assertEquals(RuntimeException.class.getName(), response.getException());
        assertEquals(errorMessage, response.getMessage());
    }

    @Test
    @DisplayName("Проверка работы Lombok методов (сеттеры, equals, hashCode, toString)")
    void lombokMethods_ShouldWorkCorrectly() {
        Exception ex1 = new IllegalArgumentException("Error 1");
        Response response1 = new Response(ex1);

        response1.setException("CustomException");
        response1.setMessage("Custom Message");

        assertEquals("CustomException", response1.getException());
        assertEquals("Custom Message", response1.getMessage());

        Response response2 = new Response(new Exception());
        response2.setException("CustomException");
        response2.setMessage("Custom Message");

        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());

        assertNotNull(response1.toString());
        assertTrue(response1.toString().contains("CustomException"));
    }
}
