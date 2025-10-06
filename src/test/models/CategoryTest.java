package test.models;

import main.errors.BadRequestException;
import main.models.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category(1L, "Food", "All food items");
    }

    @Test
    void constructorShouldSetFieldsCorrectly() {
        assertEquals(1, category.getId());
        assertEquals("Food", category.getName());
        assertEquals("All food items", category.getDescription());
    }

    @Test
    void constructorWithoutDescriptionShouldSetEmptyDescription() {
        Category categoryNoDesc = new Category(2L, "Drink");
        assertEquals(2, categoryNoDesc.getId());
        assertEquals("Drink", categoryNoDesc.getName());
        assertEquals("", categoryNoDesc.getDescription());
    }

    @Test
    void setNameShouldSetName() {
        category.setName("Drink");
        assertEquals("Drink", category.getName());
    }

    @Test
    void setNameShouldThrowOnNullOrEmpty() {
        assertThrows(BadRequestException.class, () -> category.setName(null));
        assertThrows(BadRequestException.class, () -> category.setName(""));
        assertThrows(BadRequestException.class, () -> category.setName("   "));
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
        category.setId(7L);
        assertEquals(7, category.getId());
    }

    @Test
    void setIdShouldThrowOnNull() {
        assertThrows(BadRequestException.class, () -> category.setId(null));
    }

    @Test
    void setCreatedAtShouldThrowOnNullOrFuture() {
        assertThrows(BadRequestException.class, () -> category.setCreatedAt(null));
        assertThrows(BadRequestException.class, () -> category.setCreatedAt(LocalDateTime.now().plusDays(1)));
    }

    @Test
    void setCreatedAtShouldAcceptValidDate() {
        LocalDateTime now = LocalDateTime.now();
        category.setCreatedAt(now);
        assertEquals(now, category.getCreatedAt());
    }

    @Test
    void setUpdatedAtShouldThrowOnNullOrFuture() {
        assertThrows(BadRequestException.class, () -> category.setUpdatedAt(null));
        assertThrows(BadRequestException.class, () -> category.setUpdatedAt(LocalDateTime.now().plusDays(1)));
    }

    @Test
    void setUpdatedAtShouldAcceptValidDate() {
        LocalDateTime now = LocalDateTime.now();
        category.setUpdatedAt(now);
        assertEquals(now, category.getUpdateAt());
    }
}
