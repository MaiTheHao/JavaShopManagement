package test.models;

import main.errors.BadRequestException;
import main.models.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User(1L, "test@example.com", "password123", "Test User");
    }

    @Test
    void constructorShouldSetFieldsCorrectly() {
        assertEquals("test@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
        assertEquals("Test User", user.getName());
        assertEquals(1, user.getId());
    }

    @Test
    void setEmailShouldThrowOnNullOrEmpty() {
        assertThrows(BadRequestException.class, () -> user.setEmail(null));
        assertThrows(BadRequestException.class, () -> user.setEmail(""));
    }

    @Test
    void setEmailShouldAcceptValidEmail() {
        user.setEmail("new@example.com");
        assertEquals("new@example.com", user.getEmail());
    }

    @Test
    void setPasswordShouldThrowOnNullOrEmpty() {
        assertThrows(BadRequestException.class, () -> user.setPassword(null));
        assertThrows(BadRequestException.class, () -> user.setPassword(""));
    }

    @Test
    void setPasswordShouldAcceptValidPassword() {
        user.setPassword("newpassaa");
        assertEquals("newpassaa", user.getPassword());
    }

    @Test
    void setNameShouldThrowOnNullOrEmpty() {
        assertThrows(BadRequestException.class, () -> user.setName(null));
        assertThrows(BadRequestException.class, () -> user.setName(""));
        assertThrows(BadRequestException.class, () -> user.setName("   "));
    }

    @Test
    void setNameShouldAcceptValidName() {
        user.setName("Alice");
        assertEquals("Alice", user.getName());
    }

    @Test
    void setIdShouldSetId() {
        user.setId(42L);
        assertEquals(42, user.getId());
    }

    @Test
    void setCreatedAtShouldThrowOnNull() {
        assertThrows(BadRequestException.class, () -> user.setCreatedAt(null));
    }

    @Test
    void setCreatedAtShouldThrowOnFutureDate() {
        LocalDateTime future = LocalDateTime.now().plusDays(1);
        assertThrows(BadRequestException.class, () -> user.setCreatedAt(future));
    }

    @Test
    void setCreatedAtShouldAcceptValidDate() {
        LocalDateTime now = LocalDateTime.now();
        user.setCreatedAt(now);
        assertEquals(now, user.getCreatedAt());
    }

    @Test
    void setUpdatedAtShouldThrowOnNull() {
        assertThrows(BadRequestException.class, () -> user.setUpdatedAt(null));
    }

    @Test
    void setUpdatedAtShouldThrowOnFutureDate() {
        LocalDateTime future = LocalDateTime.now().plusDays(1);
        assertThrows(BadRequestException.class, () -> user.setUpdatedAt(future));
    }

    @Test
    void setUpdatedAtShouldAcceptValidDate() {
        LocalDateTime now = LocalDateTime.now();
        user.setUpdatedAt(now);
        assertEquals(now, user.getUpdateAt());
    }
}