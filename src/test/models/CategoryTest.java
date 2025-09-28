package test.models;

import main.models.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category("Food", "All food items");
    }

    @Test
    void constructorShouldSetFieldsCorrectly() {
        assertEquals("Food", category.getName());
        assertEquals("All food items", category.getDescription());
    }

    @Test
    void setNameShouldSetName() {
        category.setName("Drink");
        assertEquals("Drink", category.getName());
    }

    @Test
    void setNameShouldThrowOnNullOrEmpty() {
        assertThrows(IllegalArgumentException.class, () -> category.setName(null));
        assertThrows(IllegalArgumentException.class, () -> category.setName(""));
        assertThrows(IllegalArgumentException.class, () -> category.setName("   "));
    }

    @Test
    void setDescriptionShouldSetDescription() {
        category.setDescription("Beverages");
        assertEquals("Beverages", category.getDescription());
    }

    @Test
    void setDescriptionShouldAcceptNullOrEmpty() {
        category.setDescription(null);
        assertNull(category.getDescription());
        category.setDescription("");
        assertEquals("", category.getDescription());
    }

    @Test
    void setIdShouldSetId() {
        category.setId(7);
        assertEquals(7, category.getId());
    }

    @Test
    void setCreatedAtShouldThrowOnNullOrFuture() {
        assertThrows(IllegalArgumentException.class, () -> category.setCreatedAt(null));
        assertThrows(IllegalArgumentException.class, () -> category.setCreatedAt(LocalDateTime.now().plusDays(1)));
    }

    @Test
    void setCreatedAtShouldAcceptValidDate() {
        LocalDateTime now = LocalDateTime.now();
        category.setCreatedAt(now);
        assertEquals(now, category.getCreatedAt());
    }

    @Test
    void setUpdatedAtShouldThrowOnNullOrFuture() {
        assertThrows(IllegalArgumentException.class, () -> category.setUpdatedAt(null));
        assertThrows(IllegalArgumentException.class, () -> category.setUpdatedAt(LocalDateTime.now().plusDays(1)));
    }

    @Test
    void setUpdatedAtShouldAcceptValidDate() {
        LocalDateTime now = LocalDateTime.now();
        category.setUpdatedAt(now);
        assertEquals(now, category.getUpdateAt());
    }
}
