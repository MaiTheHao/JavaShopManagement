package test.repositories;

import main.repositories.UserRepository;
import main.models.User;
import main.enumerations.Role;
import main.errors.BadRequestException;
import main.errors.NotFoundException;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {

    private UserRepository repo;

    @BeforeEach
    void setUp() {
        repo = UserRepository.getInstance(10);
        repo.clear();
    }

    @Test
    void testAddAndFindByEmail() {
        User user = new User(1L, "test@example.com", "pass012938012938210", "Test User", Role.USER);
        repo.add(user);

        User found = repo.findByEmail("test@example.com");
        assertNotNull(found);
        assertEquals("Test User", found.getName());
    }

    @Test
    void testFindByEmailCaseInsensitive() {
        User user = new User(2L, "user@domain.com", "pass012938012938210", "User2", Role.USER);
        repo.add(user);

        User found = repo.findByEmail("USER@DOMAIN.COM");
        assertNotNull(found);
        assertEquals(2L, found.getId());
    }

    @Test
    void testFindByEmailThrowsOnNullOrEmpty() {
        assertThrows(BadRequestException.class, () -> repo.findByEmail(null));
        assertThrows(BadRequestException.class, () -> repo.findByEmail(""));
    }

    @Test
    void testUpdate() {
        User user = new User(3L, "update@domain.com", "pass012938012938210", "OldName", Role.USER);
        repo.add(user);

        User updated = new User(3L, "update@domain.com", "pass012938012938210", "NewName", Role.USER);
        repo.update(updated);

        User found = repo.findById(3L);
        assertEquals("NewName", found.getName());
    }

    @Test
    void testUpdateThrowsOnNotFound() {
        User user = new User(99L, "notfound@domain.com", "pass012938012938210", "Name", Role.USER);
        assertThrows(NotFoundException.class, () -> repo.update(user));
    }

    @Test
    void testRemove() {
        User user = new User(4L, "remove@domain.com", "pass012938012938210", "ToRemove", Role.USER);
        repo.add(user);
        repo.remove(4L);

        assertThrows(NotFoundException.class, () -> repo.findById(4L));
    }

    @Test
    void testClear() {
        repo.add(new User(5L, "a@b.com", "pass012938012938210", "A", Role.USER));
        repo.add(new User(6L, "b@c.com", "pass012938012938210", "B", Role.USER));
        repo.clear();
        assertEquals(0, repo.getSize());
    }

    @Test
    void testCapacityIncrease() {
        for (int i = 0; i < 15; i++) {
            repo.add(new User((long) i, "u" + i + "@mail.com", "pass012938012938210", "User" + i, Role.USER));
        }
        assertTrue(repo.getCapacity() >= 15);
        assertEquals(15, repo.getSize());
    }
}
