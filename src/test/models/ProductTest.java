package test.models;

import main.errors.BadRequestException;
import main.models.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product(1L, "Test Product", 10.5, 5, 2, "Sample description");
    }

    @Test
    void testProductConstructorAndGetters() {
        assertEquals(1, product.getId());
        assertEquals("Test Product", product.getName());
        assertEquals(10.5, product.getPrice());
        assertEquals(5, product.getQuantity());
        assertEquals(2, product.getCategoryId());
        assertEquals("Sample description", product.getDescription());
    }

    @Test
    void testSetPriceValid() {
        product.setPrice(20.0);
        assertEquals(20.0, product.getPrice());
    }

    @Test
    void testSetPriceInvalid() {
        assertThrows(BadRequestException.class, () -> product.setPrice(-1.0));
    }

    @Test
    void testSetQuantityValid() {
        product.setQuantity(10);
        assertEquals(10, product.getQuantity());
    }

    @Test
    void testSetQuantityInvalidNull() {
        assertThrows(BadRequestException.class, () -> product.setQuantity(null));
    }

    @Test
    void testSetQuantityInvalidNegative() {
        assertThrows(BadRequestException.class, () -> product.setQuantity(-5));
    }

    @Test
    void testIncreaseQuantityPositiveAmount() {
        product.inceaseQuantity(3);
        assertEquals(8, product.getQuantity());
    }

    @Test
    void testIncreaseQuantityZeroOrNegativeAmount() {
        product.inceaseQuantity(0);
        assertEquals(5, product.getQuantity());
        product.inceaseQuantity(-2);
        assertEquals(5, product.getQuantity());
    }

    @Test
    void testDecreaseQuantityPositiveAmount() {
        product.decreaseQuantity(2);
        assertEquals(3, product.getQuantity());
    }

    @Test
    void testDecreaseQuantityZeroOrNegativeAmount() {
        product.decreaseQuantity(0);
        assertEquals(5, product.getQuantity());
        product.decreaseQuantity(-1);
        assertEquals(5, product.getQuantity());
    }

    @Test
    void testDecreaseQuantityToZero() {
        product.decreaseQuantity(10);
        assertEquals(0, product.getQuantity());
    }

    @Test
    void testIsOutOfStockTrue() {
        product.setQuantity(0);
        assertTrue(product.isOutOfStock());
    }

    @Test
    void testIsOutOfStockFalse() {
        product.setQuantity(1);
        assertFalse(product.isOutOfStock());
    }

    @Test
    void testSetCategoryId() {
        product.setCategoryId(5);
        assertEquals(5, product.getCategoryId());
    }

    @Test
    void testSetDescription() {
        product.setDescription("New description");
        assertEquals("New description", product.getDescription());
    }

    @Test
    void testSetIdValid() {
        product.setId(100L);
        assertEquals(100, product.getId());
    }

    @Test
    void testSetIdInvalidNull() {
        assertThrows(BadRequestException.class, () -> product.setId(null));
    }

    @Test
    void testSetNameValid() {
        product.setName("New Name");
        assertEquals("New Name", product.getName());
    }

    @Test
    void testSetNameInvalidNull() {
        assertThrows(BadRequestException.class, () -> product.setName(null));
    }

    @Test
    void testSetNameInvalidEmpty() {
        assertThrows(BadRequestException.class, () -> product.setName("   "));
    }

    @Test
    void testSetCreatedAtValid() {
        LocalDateTime now = LocalDateTime.now();
        product.setCreatedAt(now.minusDays(1));
        assertEquals(now.minusDays(1), product.getCreatedAt());
    }

    @Test
    void testSetCreatedAtNull() {
        assertThrows(BadRequestException.class, () -> product.setCreatedAt(null));
    }

    @Test
    void testSetCreatedAtFuture() {
        LocalDateTime future = LocalDateTime.now().plusDays(1);
        assertThrows(BadRequestException.class, () -> product.setCreatedAt(future));
    }

    @Test
    void testSetUpdatedAtValid() {
        LocalDateTime now = LocalDateTime.now();
        product.setUpdatedAt(now.minusHours(1));
        assertEquals(now.minusHours(1), product.getUpdateAt());
    }

    @Test
    void testSetUpdatedAtNull() {
        assertThrows(BadRequestException.class, () -> product.setUpdatedAt(null));
    }

    @Test
    void testSetUpdatedAtFuture() {
        LocalDateTime future = LocalDateTime.now().plusHours(1);
        assertThrows(BadRequestException.class, () -> product.setUpdatedAt(future));
    }
}