package test.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import main.services.AuthService;
import main.services.SessionService;
import main.repositories.UserRepository;
import main.models.User;
import main.errors.BadRequestException;
import main.interfaces.services.ISessionService;

public class AuthServiceTest {

    private AuthService authService;
    private UserRepository userRepository;
    private ISessionService sessionService;
    private User testUser;

    @BeforeEach
    void setUp() throws Exception {
        authService = AuthService.getInstance();
        sessionService = SessionService.getInstance();
        userRepository = UserRepository.getInstance();

        testUser = new User(1L, "test@example.com", "password123", "Test User");
        userRepository.add(testUser);
    }

    @Test
    @DisplayName("Should return singleton instance")
    void testGetInstance() {
        AuthService instance1 = AuthService.getInstance();
        AuthService instance2 = AuthService.getInstance();
        assertSame(instance1, instance2);
    }

    @Test
    @DisplayName("Should login successfully with valid credentials")
    void testLoginSuccess() throws Exception {
        User result = authService.login("test@example.com", "password123");

        assertNotNull(result);
        assertEquals(testUser.getEmail(), result.getEmail());
    }

    @Test
    @DisplayName("Should throw BadRequestException for null email")
    void testLoginNullEmail() {
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> authService.login(null, "password"));
        assertEquals("Email cannot be null or empty.", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw BadRequestException for empty email")
    void testLoginEmptyEmail() {
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> authService.login("", "password"));
        assertEquals("Email cannot be null or empty.", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw BadRequestException for null password")
    void testLoginNullPassword() {
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> authService.login("test@example.com", null));
        assertEquals("Password cannot be null or empty.", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw BadRequestException for empty password")
    void testLoginEmptyPassword() {
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> authService.login("test@example.com", ""));
        assertEquals("Password cannot be null or empty.", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw BadRequestException for user not found")
    void testLoginUserNotFound() throws Exception {
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> authService.login("nonexistent@example.com", "password"));
        assertEquals("Invalid email or password.", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw BadRequestException for wrong password")
    void testLoginWrongPassword() throws Exception {
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> authService.login("test@example.com", "wrongpassword"));
        assertEquals("Invalid email or password.", exception.getMessage());
    }

    @Test
    @DisplayName("Should register successfully with valid data")
    void testRegisterSuccess() throws Exception {
        authService.register("new@example.com", "password123", "password123", "New User");

        User newUser = userRepository.findByEmail("new@example.com");
        assertNotNull(newUser);
        assertEquals("new@example.com", newUser.getEmail());
    }

    @Test
    @DisplayName("Should throw BadRequestException for null email in register")
    void testRegisterNullEmail() {
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> authService.register(null, "password", "password", "name"));
        assertEquals("Email cannot be null or empty.", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw BadRequestException for null password in register")
    void testRegisterNullPassword() {
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> authService.register("test@example.com", null, "password", "name"));
        assertEquals("Password cannot be null or empty.", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw BadRequestException for null name in register")
    void testRegisterNullName() {
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> authService.register("test@example.com", "password", "password", null));
        assertEquals("Name cannot be null or empty.", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw BadRequestException for password mismatch")
    void testRegisterPasswordMismatch() {
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> authService.register("test@example.com", "password1", "password2", "name"));
        assertEquals("Passwords do not match.", exception.getMessage());
    }

    @Test
    @DisplayName("Should logout successfully")
    void testLogout() throws Exception {
        User result = authService.login("test@example.com", "password123");
        sessionService.create(result.getId());
        authService.logout(result.getId());
        assertFalse(sessionService.isActive(result.getId()));
    }

    @Test
    @DisplayName("Should handle logout when no current user")
    void testLogoutNoUser() {
        assertDoesNotThrow(() -> authService.logout(9L));
    }

    @Test
    @DisplayName("Should return true when session is active")
    void testIsSessionActiveTrue() throws Exception {
        User result = authService.login("test@example.com", "password123");
        sessionService.create(result.getId());
        assertTrue(authService.validSession(result.getId()));
    }

    @Test
    @DisplayName("Should return false when no session")
    void testIsSessionActiveFalse() {
        assertFalse(authService.validSession(9L));
    }
}