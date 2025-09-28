package test.repositories;

import static org.junit.jupiter.api.Assertions.*;
import main.models.User;
import main.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

class UserRepositoryTest {

    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = UserRepository.getInstance();
        userRepository.clear();
    }

    @AfterEach
    void tearDown() {
        userRepository = null;
    }

    @Test
    void shouldReturnSingletonInstance() {
        UserRepository repo1 = UserRepository.getInstance();
        UserRepository repo2 = UserRepository.getInstance();
        assertSame(repo1, repo2);
    }

    @Test
    void shouldReturnSingletonInstanceWithCapacity() {
        UserRepository repo1 = UserRepository.getInstance(100);
        UserRepository repo2 = UserRepository.getInstance(200);
        assertSame(repo1, repo2);
    }

    @Test
    void shouldFindUserByEmailCaseInsensitive() {
        User user1 = new User("john@example.com", "John", "password");
        User user2 = new User("jane@example.com", "Jane", "password");

        userRepository.add(user1);
        userRepository.add(user2);

        User found = userRepository.findByEmail("JOHN@EXAMPLE.COM");
        assertEquals(user1, found);
        assertEquals("john@example.com", found.getEmail());
    }

    @Test
    void shouldReturnNullWhenUserNotFoundByEmail() {
        User user = new User("john@example.com", "John", "password");
        userRepository.add(user);

        User found = userRepository.findByEmail("notfound@example.com");
        assertNull(found);
    }

    @Test
    void shouldFindUserByExactEmailMatch() {
        User user = new User("test@example.com", "Test User", "password");
        userRepository.add(user);

        User found = userRepository.findByEmail("test@example.com");
        assertEquals(user, found);
    }

    @Test
    void shouldHandleEmptyRepositoryWhenFindingByEmail() {
        User found = userRepository.findByEmail("any@example.com");
        assertNull(found);
    }

    @Test
    void shouldFindFirstUserWhenMultipleUsersWithSameEmail() {
        User user1 = new User("same@example.com", "User1", "password1");
        User user2 = new User("same@example.com", "User2", "password2");

        userRepository.add(user1);
        userRepository.add(user2);

        User found = userRepository.findByEmail("same@example.com");
        assertEquals(user1, found);
    }

    @Test
    void shouldHandleNullEmailInFindByEmail() {
        User user = new User("test@example.com", "Test", "password");
        userRepository.add(user);

        User found = userRepository.findByEmail(null);
        assertNull(found);
    }

    @Test
    void shouldHandleEmptyStringEmailInFindByEmail() {
        User user = new User("test@example.com", "Test", "password");
        userRepository.add(user);

        User found = userRepository.findByEmail("");
        assertNull(found);
    }

    @Test
    void shouldMaintainRepositoryStateAfterQuery() {
        User user1 = new User("user1@example.com", "User1", "password");
        User user2 = new User("user2@example.com", "User2", "password");

        userRepository.add(user1);
        userRepository.add(user2);

        assertEquals(2, userRepository.getSize());

        userRepository.findByEmail("user1@example.com");

        assertEquals(2, userRepository.getSize());
    }

    @Test
    void shouldWorkWithQueryOperations() {
        User user1 = new User("admin@example.com", "Admin", "password");
        User user2 = new User("user@example.com", "User", "password");
        User user3 = new User("guest@example.com", "Guest", "password");

        userRepository.add(user1);
        userRepository.add(user2);
        userRepository.add(user3);

        User[] filtered = userRepository.query()
                .filter(u -> u.getEmail().contains("user"))
                .getResult();

        assertEquals(1, filtered.length);
        assertEquals(user2, filtered[0]);
    }
}
