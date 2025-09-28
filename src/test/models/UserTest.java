package test.models;

import main.models.User;
import main.models.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;
    private Role defaultRole;

    @BeforeEach
    void setUp() {
        defaultRole = new Role("User", "Normal user");
        user = new User("test@example.com", "password123", defaultRole, "Test User");
    }

    @Test
    void constructorShouldSetFieldsCorrectly() {
        assertEquals("test@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
        assertEquals("Test User", user.getName());
        assertEquals(defaultRole, user.getRole());
    }

    @Test
    void constructorWithRoleShouldSetFieldsCorrectly() {
        Role role = new Role("Admin", "Administrator");
        User u = new User("admin@example.com", "adminpass", role, "Admin");
        assertEquals("admin@example.com", u.getEmail());
        assertEquals("adminpass", u.getPassword());
        assertEquals("Admin", u.getName());
        assertEquals(role, u.getRole());
    }

    @Test
    void setEmailShouldThrowOnNullOrEmpty() {
        assertThrows(IllegalArgumentException.class, () -> user.setEmail(null));
        assertThrows(IllegalArgumentException.class, () -> user.setEmail(""));
    }

    @Test
    void setEmailShouldAcceptValidEmail() {
        user.setEmail("new@example.com");
        assertEquals("new@example.com", user.getEmail());
    }

    @Test
    void setPasswordShouldThrowOnNullOrEmpty() {
        assertThrows(IllegalArgumentException.class, () -> user.setPassword(null));
        assertThrows(IllegalArgumentException.class, () -> user.setPassword(""));
    }

    @Test
    void setPasswordShouldAcceptValidPassword() {
        user.setPassword("newpass");
        assertEquals("newpass", user.getPassword());
    }

    @Test
    void setRoleShouldThrowOnNull() {
        assertThrows(IllegalArgumentException.class, () -> user.setRole(null));
    }

    @Test
    void setRoleShouldAcceptValidRole() {
        Role role = new Role("Manager", "Manager role");
        user.setRole(role);
        assertEquals(role, user.getRole());
    }

    @Test
    void setNameShouldThrowOnNullOrEmpty() {
        assertThrows(IllegalArgumentException.class, () -> user.setName(null));
        assertThrows(IllegalArgumentException.class, () -> user.setName(""));
        assertThrows(IllegalArgumentException.class, () -> user.setName("   "));
    }

    @Test
    void setNameShouldAcceptValidName() {
        user.setName("Alice");
        assertEquals("Alice", user.getName());
    }

    @Test
    void setIdShouldSetId() {
        user.setId(42);
        assertEquals(42, user.getId());
    }

    @Test
    void setCreatedAtShouldThrowOnNull() {
        assertThrows(IllegalArgumentException.class, () -> user.setCreatedAt(null));
    }

    @Test
    void setCreatedAtShouldThrowOnFutureDate() {
        LocalDateTime future = LocalDateTime.now().plusDays(1);
        assertThrows(IllegalArgumentException.class, () -> user.setCreatedAt(future));
    }

    @Test
    void setCreatedAtShouldAcceptValidDate() {
        LocalDateTime now = LocalDateTime.now();
        user.setCreatedAt(now);
        assertEquals(now, user.getCreatedAt());
    }

    @Test
    void setUpdatedAtShouldThrowOnNull() {
        assertThrows(IllegalArgumentException.class, () -> user.setUpdatedAt(null));
    }

    @Test
    void setUpdatedAtShouldThrowOnFutureDate() {
        LocalDateTime future = LocalDateTime.now().plusDays(1);
        assertThrows(IllegalArgumentException.class, () -> user.setUpdatedAt(future));
    }

    @Test
    void setUpdatedAtShouldAcceptValidDate() {
        LocalDateTime now = LocalDateTime.now();
        user.setUpdatedAt(now);
        assertEquals(now, user.getUpdateAt());
    }
}
