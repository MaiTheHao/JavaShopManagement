package test.models;

import main.models.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class RoleTest {

    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role("User", "Normal user");
    }

    @Test
    void constructorShouldSetFieldsCorrectly() {
        assertEquals("User", role.getName());
        assertEquals("Normal user", role.getDescription());
    }

    @Test
    void setNameShouldSetName() {
        role.setName("Admin");
        assertEquals("Admin", role.getName());
    }

    @Test
    void setDescriptionShouldSetDescription() {
        role.setDescription("Administrator");
        assertEquals("Administrator", role.getDescription());
    }

    @Test
    void setNameShouldThrowOnNullOrEmpty() {
        assertThrows(IllegalArgumentException.class, () -> role.setName(null));
        assertThrows(IllegalArgumentException.class, () -> role.setName(""));
        assertThrows(IllegalArgumentException.class, () -> role.setName("   "));
    }

    @Test
    void setDescriptionShouldAcceptNullOrEmpty() {
        role.setDescription(null);
        assertNull(role.getDescription());
        role.setDescription("");
        assertEquals("", role.getDescription());
    }

    @Test
    void setIdShouldSetId() {
        role.setId(10);
        assertEquals(10, role.getId());
    }
}
