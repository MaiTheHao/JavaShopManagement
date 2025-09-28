package test.repositories;

import static org.junit.jupiter.api.Assertions.*;
import main.models.Role;
import main.repositories.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import java.time.LocalDateTime;

class RoleRepositoryTest {

    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        roleRepository = RoleRepository.getInstance();
        roleRepository.clear();
    }

    @AfterEach
    void tearDown() {
        roleRepository = null;
    }

    @Test
    void shouldReturnSingletonInstance() {
        RoleRepository repo1 = RoleRepository.getInstance();
        RoleRepository repo2 = RoleRepository.getInstance();
        assertSame(repo1, repo2);
    }

    @Test
    void shouldReturnSingletonInstanceWithCapacity() {
        RoleRepository repo1 = RoleRepository.getInstance(100);
        RoleRepository repo2 = RoleRepository.getInstance(200);
        assertSame(repo1, repo2);
    }

    @Test
    void shouldAddRoleSuccessfully() {
        Role role = new Role("Admin");
        role.setId(1);

        roleRepository.add(role);

        assertEquals(1, roleRepository.getSize());
        assertEquals(role, roleRepository.findById(1));
    }

    @Test
    void shouldUpdateRoleSuccessfully() {
        Role role = new Role("User");
        role.setId(1);
        roleRepository.add(role);

        Role updatedRole = new Role("Super User");
        updatedRole.setId(1);

        roleRepository.update(updatedRole);

        assertEquals("Super User", roleRepository.findById(1).getName());
    }

    @Test
    void shouldRemoveRoleSuccessfully() {
        Role role1 = new Role("Admin");
        role1.setId(1);
        Role role2 = new Role("User");
        role2.setId(2);

        roleRepository.add(role1);
        roleRepository.add(role2);

        roleRepository.remove(1);

        assertEquals(1, roleRepository.getSize());
        assertNull(roleRepository.findById(1));
        assertNotNull(roleRepository.findById(2));
    }

    @Test
    void shouldFindRoleById() {
        Role role = new Role("Manager");
        role.setId(5);
        roleRepository.add(role);

        Role found = roleRepository.findById(5);
        assertEquals(role, found);
    }

    @Test
    void shouldReturnNullWhenRoleNotFoundById() {
        Role found = roleRepository.findById(999);
        assertNull(found);
    }

    @Test
    void shouldFindRoleByName() {
        Role role = new Role("Supervisor");
        role.setId(1);
        roleRepository.add(role);

        Role found = roleRepository.findByName("Supervisor");
        assertEquals(role, found);
    }

    @Test
    void shouldFindRoleByNameCaseInsensitive() {
        Role role = new Role("Moderator");
        role.setId(1);
        roleRepository.add(role);

        Role found = roleRepository.findByName("MODERATOR");
        assertEquals(role, found);
    }

    @Test
    void shouldCheckIfRoleExists() {
        Role role = new Role("Guest");
        role.setId(3);
        roleRepository.add(role);

        assertTrue(roleRepository.exists(3));
        assertFalse(roleRepository.exists(99));
    }

    @Test
    void shouldSortRolesByName() {
        Role role1 = new Role("Zebra Role");
        role1.setId(1);
        Role role2 = new Role("Admin");
        role2.setId(2);
        Role role3 = new Role("Manager");
        role3.setId(3);

        roleRepository.add(role1);
        roleRepository.add(role2);
        roleRepository.add(role3);

        Role[] sorted = roleRepository.sortByName().getResult();

        assertEquals("Admin", sorted[0].getName());
        assertEquals("Manager", sorted[1].getName());
        assertEquals("Zebra Role", sorted[2].getName());
    }

    @Test
    void shouldSortRolesByCreatedAt() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime earlier = now.minusHours(2);
        LocalDateTime latest = now.minusHours(1);

        Role role1 = new Role("Recent Role");
        role1.setId(1);
        role1.setCreatedAt(now);

        Role role2 = new Role("Old Role");
        role2.setId(2);
        role2.setCreatedAt(earlier);

        Role role3 = new Role("Middle Role");
        role3.setId(3);
        role3.setCreatedAt(latest);

        roleRepository.add(role1);
        roleRepository.add(role2);
        roleRepository.add(role3);

        Role[] sorted = roleRepository.sortByCreatedAt().getResult();

        assertEquals("Old Role", sorted[0].getName());
        assertEquals("Middle Role", sorted[1].getName());
        assertEquals("Recent Role", sorted[2].getName());
    }

    @Test
    void shouldHandleEmptyRepository() {
        assertEquals(0, roleRepository.getSize());
        assertNull(roleRepository.findById(1));
        assertNull(roleRepository.findByName("Any"));
        assertEquals(0, roleRepository.query().getSize());
    }

    @Test
    void shouldClearRepository() {
        Role role1 = new Role("Role1");
        role1.setId(1);
        Role role2 = new Role("Role2");
        role2.setId(2);

        roleRepository.add(role1);
        roleRepository.add(role2);

        assertEquals(2, roleRepository.getSize());

        roleRepository.clear();

        assertEquals(0, roleRepository.getSize());
    }

    @Test
    void shouldWorkWithQueryOperations() {
        Role role1 = new Role("System Admin");
        role1.setId(1);
        Role role2 = new Role("User");
        role2.setId(2);
        Role role3 = new Role("System Manager");
        role3.setId(3);

        roleRepository.add(role1);
        roleRepository.add(role2);
        roleRepository.add(role3);

        Role[] filtered = roleRepository.query()
                .filter(r -> r.getName().toLowerCase().contains("system"))
                .getResult();

        assertEquals(2, filtered.length);
    }

    @Test
    void shouldHandleNullUpdate() {
        Role role = new Role("Test Role");
        role.setId(1);
        roleRepository.add(role);

        roleRepository.update(null);

        assertEquals(1, roleRepository.getSize());
        assertEquals("Test Role", roleRepository.findById(1).getName());
    }

    @Test
    void shouldCheckDuplication() {
        Role role = new Role("Unique Role");
        role.setId(10);
        roleRepository.add(role);

        assertTrue(roleRepository.isDuplicated(role));

        Role newRole = new Role("Another Role");
        newRole.setId(20);

        assertFalse(roleRepository.isDuplicated(newRole));
    }
}
