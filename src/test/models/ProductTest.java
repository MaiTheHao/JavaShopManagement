package test.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import main.models.Product;
import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product("Milk", 10.5, 20);
    }

    @Test
    void constructorShouldSetFieldsCorrectly() {
        assertEquals("Milk", product.getName());
        assertEquals(10.5, product.getPrice());
        assertEquals(20, product.getQuantity());
    }

    @Test
    void setNameShouldSetName() {
        product.setName("Bread");
        assertEquals("Bread", product.getName());
    }

    @Test
    void setNameShouldThrowOnNullOrEmpty() {
        assertThrows(IllegalArgumentException.class, () -> product.setName(null));
        assertThrows(IllegalArgumentException.class, () -> product.setName(""));
        assertThrows(IllegalArgumentException.class, () -> product.setName("   "));
    }

    @Test
    void setPriceShouldThrowOnNegative() {
        assertThrows(IllegalArgumentException.class, () -> product.setPrice(-1));
    }

    @Test
    void setPriceShouldAcceptZeroOrPositive() {
        product.setPrice(0);
        assertEquals(0, product.getPrice());
        product.setPrice(5.5);
        assertEquals(5.5, product.getPrice());
    }

    @Test
    void setQuantityShouldThrowOnNegative() {
        assertThrows(IllegalArgumentException.class, () -> product.setQuantity(-10));
    }

    @Test
    void setQuantityShouldAcceptZeroOrPositive() {
        product.setQuantity(0);
        assertEquals(0, product.getQuantity());
        product.setQuantity(100);
        assertEquals(100, product.getQuantity());
    }

    @Test
    void setCategoryIdShouldSetCategoryId() {
        product.setCategoryId(5);
        assertEquals(5, product.getCategoryId());
    }

    @Test
    void setDescriptionShouldSetDescription() {
        product.setDescription("Fresh milk");
        assertEquals("Fresh milk", product.getDescription());
    }

    @Test
    void increaseQuantityShouldIncrease() {
        product.inceaseQuantity(10);
        assertEquals(30, product.getQuantity());
    }

    @Test
    void increaseQuantityShouldNotChangeIfNonPositive() {
        product.inceaseQuantity(0);
        assertEquals(20, product.getQuantity());
        product.inceaseQuantity(-5);
        assertEquals(20, product.getQuantity());
    }

    @Test
    void decreaseQuantityShouldDecrease() {
        product.decreaseQuantity(5);
        assertEquals(15, product.getQuantity());
    }

    @Test
    void decreaseQuantityShouldNotGoBelowZero() {
        product.decreaseQuantity(100);
        assertEquals(0, product.getQuantity());
    }

    @Test
    void decreaseQuantityShouldNotChangeIfNonPositive() {
        product.decreaseQuantity(0);
        assertEquals(20, product.getQuantity());
        product.decreaseQuantity(-3);
        assertEquals(20, product.getQuantity());
    }

    @Test
    void isOutOfStockShouldReturnTrueIfZero() {
        product.setQuantity(0);
        assertTrue(product.isOutOfStock());
    }

    @Test
    void isOutOfStockShouldReturnFalseIfPositive() {
        product.setQuantity(5);
        assertFalse(product.isOutOfStock());
    }

    @Test
    void setIdShouldSetId() {
        product.setId(123);
        assertEquals(123, product.getId());
    }

    @Test
    void setCreatedAtShouldThrowOnNullOrFuture() {
        assertThrows(IllegalArgumentException.class, () -> product.setCreatedAt(null));
        assertThrows(IllegalArgumentException.class, () -> product.setCreatedAt(LocalDateTime.now().plusDays(1)));
    }

    @Test
    void setCreatedAtShouldAcceptValidDate() {
        LocalDateTime now = LocalDateTime.now();
        product.setCreatedAt(now);
        assertEquals(now, product.getCreatedAt());
    }

    @Test
    void setUpdatedAtShouldThrowOnNullOrFuture() {
        assertThrows(IllegalArgumentException.class, () -> product.setUpdatedAt(null));
        assertThrows(IllegalArgumentException.class, () -> product.setUpdatedAt(LocalDateTime.now().plusDays(1)));
    }

    @Test
    void setUpdatedAtShouldAcceptValidDate() {
        LocalDateTime now = LocalDateTime.now();
        product.setUpdatedAt(now);
        assertEquals(now, product.getUpdateAt());
    }
}