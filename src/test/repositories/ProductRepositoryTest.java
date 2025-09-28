package test.repositories;

import static org.junit.jupiter.api.Assertions.*;
import main.models.Product;
import main.repositories.ProductRepository;
import main.enumerations.SortOrder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

class ProductRepositoryTest {

    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository = ProductRepository.getInstance();
        productRepository.clear();
    }

    @AfterEach
    void tearDown() {
        productRepository = null;
    }

    @Test
    void shouldReturnSingletonInstance() {
        ProductRepository repo1 = ProductRepository.getInstance();
        ProductRepository repo2 = ProductRepository.getInstance();
        assertSame(repo1, repo2);
    }

    @Test
    void shouldReturnSingletonInstanceWithCapacity() {
        ProductRepository repo1 = ProductRepository.getInstance(100);
        ProductRepository repo2 = ProductRepository.getInstance(200);
        assertSame(repo1, repo2);
    }

    @Test
    void shouldAddProductSuccessfully() {
        Product product = new Product("Laptop", 1500.0, 10);
        product.setId(1);

        productRepository.add(product);

        assertEquals(1, productRepository.getSize());
        assertEquals(product, productRepository.findById(1));
    }

    @Test
    void shouldUpdateProductSuccessfully() {
        Product product = new Product("Phone", 800.0, 20);
        product.setId(1);
        productRepository.add(product);

        Product updatedProduct = new Product("Updated Phone", 900.0, 25);
        updatedProduct.setId(1);

        productRepository.update(updatedProduct);

        Product found = productRepository.findById(1);
        assertEquals("Updated Phone", found.getName());
        assertEquals(900.0, found.getPrice());
        assertEquals(25, found.getQuantity());
    }

    @Test
    void shouldRemoveProductSuccessfully() {
        Product product1 = new Product("Product1", 100.0, 5);
        product1.setId(1);
        Product product2 = new Product("Product2", 200.0, 10);
        product2.setId(2);

        productRepository.add(product1);
        productRepository.add(product2);

        productRepository.remove(1);

        assertEquals(1, productRepository.getSize());
        assertNull(productRepository.findById(1));
        assertNotNull(productRepository.findById(2));
    }

    @Test
    void shouldFindProductById() {
        Product product = new Product("Tablet", 500.0, 15);
        product.setId(5);
        productRepository.add(product);

        Product found = productRepository.findById(5);
        assertEquals(product, found);
    }

    @Test
    void shouldFindProductByName() {
        Product product = new Product("Gaming Mouse", 50.0, 30);
        product.setId(1);
        productRepository.add(product);

        Product found = productRepository.findByName("Gaming Mouse");
        assertEquals(product, found);
    }

    @Test
    void shouldSortProductsByPriceAscending() {
        Product product1 = new Product("Expensive", 1000.0, 1);
        product1.setId(1);
        Product product2 = new Product("Cheap", 10.0, 50);
        product2.setId(2);
        Product product3 = new Product("Medium", 100.0, 20);
        product3.setId(3);

        productRepository.add(product1);
        productRepository.add(product2);
        productRepository.add(product3);

        Product[] sorted = productRepository.sortByPrice().getResult();

        assertEquals(10.0, sorted[0].getPrice());
        assertEquals(100.0, sorted[1].getPrice());
        assertEquals(1000.0, sorted[2].getPrice());
    }

    @Test
    void shouldSortProductsByPriceDescending() {
        Product product1 = new Product("Cheap", 10.0, 50);
        product1.setId(1);
        Product product2 = new Product("Expensive", 1000.0, 1);
        product2.setId(2);
        Product product3 = new Product("Medium", 100.0, 20);
        product3.setId(3);

        productRepository.add(product1);
        productRepository.add(product2);
        productRepository.add(product3);

        Product[] sorted = productRepository.sortByPrice(SortOrder.DESC).getResult();

        assertEquals(1000.0, sorted[0].getPrice());
        assertEquals(100.0, sorted[1].getPrice());
        assertEquals(10.0, sorted[2].getPrice());
    }

    @Test
    void shouldSortProductsByQuantityAscending() {
        Product product1 = new Product("High Stock", 50.0, 100);
        product1.setId(1);
        Product product2 = new Product("Low Stock", 75.0, 5);
        product2.setId(2);
        Product product3 = new Product("Medium Stock", 60.0, 25);
        product3.setId(3);

        productRepository.add(product1);
        productRepository.add(product2);
        productRepository.add(product3);

        Product[] sorted = productRepository.sortByQuantity().getResult();

        assertEquals(5, sorted[0].getQuantity());
        assertEquals(25, sorted[1].getQuantity());
        assertEquals(100, sorted[2].getQuantity());
    }

    @Test
    void shouldSortProductsByQuantityDescending() {
        Product product1 = new Product("Low Stock", 75.0, 5);
        product1.setId(1);
        Product product2 = new Product("High Stock", 50.0, 100);
        product2.setId(2);
        Product product3 = new Product("Medium Stock", 60.0, 25);
        product3.setId(3);

        productRepository.add(product1);
        productRepository.add(product2);
        productRepository.add(product3);

        Product[] sorted = productRepository.sortByQuantity(SortOrder.DESC).getResult();

        assertEquals(100, sorted[0].getQuantity());
        assertEquals(25, sorted[1].getQuantity());
        assertEquals(5, sorted[2].getQuantity());
    }

    @Test
    void shouldSortProductsByName() {
        Product product1 = new Product("Zebra Product", 10.0, 5);
        product1.setId(1);
        Product product2 = new Product("Alpha Product", 20.0, 10);
        product2.setId(2);
        Product product3 = new Product("Beta Product", 15.0, 8);
        product3.setId(3);

        productRepository.add(product1);
        productRepository.add(product2);
        productRepository.add(product3);

        Product[] sorted = productRepository.sortByName().getResult();

        assertEquals("Alpha Product", sorted[0].getName());
        assertEquals("Beta Product", sorted[1].getName());
        assertEquals("Zebra Product", sorted[2].getName());
    }

    @Test
    void shouldCheckIfProductExists() {
        Product product = new Product("Test Product", 25.0, 12);
        product.setId(10);
        productRepository.add(product);

        assertTrue(productRepository.exists(10));
        assertFalse(productRepository.exists(99));
    }

    @Test
    void shouldHandleEmptyRepository() {
        assertEquals(0, productRepository.getSize());
        assertNull(productRepository.findById(1));
        assertNull(productRepository.findByName("Any"));
        assertEquals(0, productRepository.query().getSize());
    }

    @Test
    void shouldWorkWithQueryOperations() {
        Product product1 = new Product("Gaming Laptop", 1200.0, 3);
        product1.setId(1);
        Product product2 = new Product("Office Laptop", 800.0, 10);
        product2.setId(2);
        Product product3 = new Product("Gaming Mouse", 50.0, 25);
        product3.setId(3);

        productRepository.add(product1);
        productRepository.add(product2);
        productRepository.add(product3);

        Product[] filtered = productRepository.query()
                .filter(p -> p.getName().toLowerCase().contains("gaming"))
                .getResult();

        assertEquals(2, filtered.length);
    }

    @Test
    void shouldFilterProductsByPriceRange() {
        Product product1 = new Product("Cheap Item", 5.0, 100);
        product1.setId(1);
        Product product2 = new Product("Mid Range", 50.0, 20);
        product2.setId(2);
        Product product3 = new Product("Expensive Item", 500.0, 2);
        product3.setId(3);

        productRepository.add(product1);
        productRepository.add(product2);
        productRepository.add(product3);

        Product[] filtered = productRepository.query()
                .filter(p -> p.getPrice() >= 10.0 && p.getPrice() <= 100.0)
                .getResult();

        assertEquals(1, filtered.length);
        assertEquals("Mid Range", filtered[0].getName());
    }
}
