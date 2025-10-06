package test.services;

import main.models.Product;
import main.services.ProductService;
import main.enumerations.ProductSortableFields;
import main.enumerations.ProductSearchableFields;
import main.enumerations.SortOrder;
import main.errors.NotFoundException;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductServiceTest {
    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = ProductService.getInstance();
        productService.deleteAll();
    }

    Product createProduct(int id, String name, double price, int quantity) {
        Product p = new Product((long) id, name, price, quantity);
        p.setCreatedAt(LocalDateTime.now().minusDays(1));
        p.setUpdatedAt(LocalDateTime.now());
        return p;
    }

    @Test
    void testAddAndGetById() {
        Product p = createProduct(1, "Test", 10.0, 5);
        productService.add(p);
        Product found = productService.getById(1L);
        assertEquals("Test", found.getName());
        assertEquals(10.0, found.getPrice());
        assertEquals(5, found.getQuantity());
    }

    @Test
    void testAddInvalidProduct() {
        assertThrows(IllegalArgumentException.class, () -> createProduct(2, "", -5.0, -1));
    }

    @Test
    void testUpdateProduct() {
        Product p = createProduct(3, "Old", 20.0, 10);
        productService.add(p);
        p.setName("New");
        p.setPrice(30.0);
        productService.update(p);
        Product updated = productService.getById(3L);
        assertEquals("New", updated.getName());
        assertEquals(30.0, updated.getPrice());
    }

    @Test
    void testUpdateNonExistentProduct() {
        Product p = createProduct(999, "NotExist", 1.0, 1);
        assertThrows(NotFoundException.class, () -> productService.update(p));
    }

    @Test
    void testDeleteProduct() {
        Product p = createProduct(4, "DeleteMe", 15.0, 2);
        productService.add(p);
        productService.delete(4L);
        assertThrows(NotFoundException.class, () -> productService.getById(4L));
    }

    @Test
    void testDeleteNonExistentProduct() {
        assertThrows(NotFoundException.class, () -> productService.delete(999L));
    }

    @Test
    void testGetListPagination() {
        for (int i = 1; i <= 10; i++) {
            productService.add(createProduct(i, "P" + i, i * 10, i));
        }
        Product[] page1 = productService.getList(1, 5);
        assertEquals(5, page1.length);
        assertEquals("P1", page1[0].getName());
        Product[] page2 = productService.getList(2, 5);
        assertEquals(5, page2.length);
        assertEquals("P6", page2[0].getName());
    }

    @Test
    void testGetListSortByPriceDesc() {
        productService.add(createProduct(1, "A", 100, 1));
        productService.add(createProduct(2, "B", 50, 2));
        productService.add(createProduct(3, "C", 200, 3));
        Product[] sorted = productService.getList(1, 3, ProductSortableFields.PRICE, SortOrder.DESC);
        assertEquals(200, sorted[0].getPrice());
        assertEquals(100, sorted[1].getPrice());
        assertEquals(50, sorted[2].getPrice());
    }

    @Test
    void testSearchByName() {
        productService.add(createProduct(1, "Apple", 10, 1));
        productService.add(createProduct(2, "Banana", 20, 2));
        productService.add(createProduct(3, "Grape", 30, 3));
        Product[] result = productService.search("ap", ProductSearchableFields.NAME, 1, 10);
        assertEquals(2, result.length);
        assertEquals("Apple", result[0].getName());
    }

    @Test
    void testSearchWithSort() {
        productService.add(createProduct(1, "Orange", 10, 5));
        productService.add(createProduct(2, "Orange", 20, 2));
        productService.add(createProduct(3, "Orange", 5, 8));
        Product[] result = productService.search("Orange", ProductSearchableFields.NAME, 1, 10,
                ProductSortableFields.PRICE, SortOrder.ASC);
        assertEquals(3, result.length);
        assertEquals(5, result[0].getPrice());
        assertEquals(10, result[1].getPrice());
        assertEquals(20, result[2].getPrice());
    }
}
