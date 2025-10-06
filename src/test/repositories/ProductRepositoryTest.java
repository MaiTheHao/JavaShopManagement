package test.repositories;

import main.repositories.ProductRepository;
import main.models.Product;
import main.enumerations.SortOrder;
import main.errors.BadRequestException;
import main.errors.NotFoundException;
import main.utils.Query;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;

public class ProductRepositoryTest {
    private ProductRepository repository;
    private Product product1;
    private Product product2;
    private Product product3;

    @BeforeEach
    void setUp() {
        try {
            java.lang.reflect.Field instanceField = ProductRepository.class.getDeclaredField("instance");
            instanceField.setAccessible(true);
            instanceField.set(null, null);
        } catch (Exception e) {
        }

        repository = ProductRepository.getInstance();

        product1 = new Product(1L, "Laptop", 999.99, 10);
        product1.setCreatedAt(LocalDateTime.now().minusDays(3));

        product2 = new Product(2L, "Mouse", 25.50, 50);
        product2.setCreatedAt(LocalDateTime.now().minusDays(2));

        product3 = new Product(3L, "Keyboard", 75.00, 5);
        product3.setCreatedAt(LocalDateTime.now().minusDays(1));
    }

    @AfterEach
    void tearDown() {
        if (repository != null) {
            repository.clear();
        }
    }

    @Test
    void testSingletonInstance() {
        ProductRepository repo1 = ProductRepository.getInstance();
        ProductRepository repo2 = ProductRepository.getInstance();
        assertSame(repo1, repo2, "Should return the same instance");
    }

    @Test
    void testAdd() {
        repository.add(product1);
        assertEquals(1, repository.getSize());
        assertEquals(product1, repository.findById(1L));
    }

    @Test
    void testAddNull() {
        assertThrows(BadRequestException.class, () -> repository.add(null));
    }

    @Test
    void testAddMultipleProducts() {
        repository.add(product1);
        repository.add(product2);
        repository.add(product3);

        assertEquals(3, repository.getSize());
        assertEquals(product1, repository.findById(1L));
        assertEquals(product2, repository.findById(2L));
        assertEquals(product3, repository.findById(3L));
    }

    @Test
    void testUpdate() {
        repository.add(product1);

        Product updatedProduct = new Product(1L, "Updated Laptop", 1199.99, 8);
        repository.update(updatedProduct);

        Product found = repository.findById(1L);
        assertEquals("Updated Laptop", found.getName());
        assertEquals(1199.99, found.getPrice());
        assertEquals(8, found.getQuantity());
    }

    @Test
    void testUpdateNonExistent() {
        Product nonExistent = new Product(999L, "Non-existent", 100.0, 1);
        assertThrows(NotFoundException.class, () -> repository.update(nonExistent));
    }

    @Test
    void testUpdateNull() {
        assertThrows(BadRequestException.class, () -> repository.update(null));
    }

    @Test
    void testRemove() {
        repository.add(product1);
        repository.add(product2);

        repository.remove(1L);
        assertEquals(1, repository.getSize());
        assertThrows(NotFoundException.class, () -> repository.findById(1L));
        assertEquals(product2, repository.findById(2L));
    }

    @Test
    void testRemoveNonExistent() {
        assertThrows(NotFoundException.class, () -> repository.remove(999L));
    }

    @Test
    void testClear() {
        repository.add(product1);
        repository.add(product2);
        repository.add(product3);

        repository.clear();
        assertEquals(0, repository.getSize());
    }

    @Test
    void testFindById() {
        repository.add(product1);
        Product found = repository.findById(1L);
        assertEquals(product1, found);
    }

    @Test
    void testFindByIdNonExistent() {
        assertThrows(NotFoundException.class, () -> repository.findById(999L));
    }

    @Test
    void testFindByName() {
        repository.add(product1);
        Product found = repository.findByName("Laptop");
        assertEquals(product1, found);
    }

    @Test
    void testFindByNameCaseInsensitive() {
        repository.add(product1);
        Product found = repository.findByName("laptop");
        assertEquals(product1, found);
    }

    @Test
    void testFindByNameNonExistent() {
        assertThrows(NotFoundException.class, () -> repository.findByName("NonExistent"));
    }

    @Test
    void testFindByNameNull() {
        assertThrows(BadRequestException.class, () -> repository.findByName(null));
    }

    @Test
    void testFindByNameEmpty() {
        assertThrows(BadRequestException.class, () -> repository.findByName(""));
    }

    @Test
    void testFindAll() {
        repository.add(product1);
        repository.add(product2);

        Product[] all = repository.findAll();
        assertEquals(2, all.length);
        assertEquals(product1, all[0]);
        assertEquals(product2, all[1]);
    }

    @Test
    void testFindAllEmpty() {
        Product[] all = repository.findAll();
        assertEquals(0, all.length);
    }

    @Test
    void testExists() {
        repository.add(product1);
        assertTrue(repository.exists(1L));
        assertFalse(repository.exists(999L));
    }

    @Test
    void testExistsEntity() {
        repository.add(product1);
        assertTrue(repository.exists(product1));
        assertFalse(repository.exists(product2));
    }

    @Test
    void testIsDuplicated() {
        repository.add(product1);
        assertTrue(repository.isDuplicated(product1));
        assertFalse(repository.isDuplicated(product2));
        assertFalse(repository.isDuplicated(null));
    }

    @Test
    void testSortByPriceAscending() {
        repository.add(product1);
        repository.add(product2);
        repository.add(product3);

        Query<Product> sorted = repository.sortByPrice();
        Product[] result = sorted.getResult();

        assertEquals(3, result.length);
        assertEquals(product2, result[0]);
        assertEquals(product3, result[1]);
        assertEquals(product1, result[2]);
    }

    @Test
    void testSortByPriceDescending() {
        repository.add(product1);
        repository.add(product2);
        repository.add(product3);

        Query<Product> sorted = repository.sortByPrice(SortOrder.DESC);
        Product[] result = sorted.getResult();

        assertEquals(3, result.length);
        assertEquals(product1, result[0]);
        assertEquals(product3, result[1]);
        assertEquals(product2, result[2]);
    }

    @Test
    void testSortByPriceWithBaseQuery() {
        repository.add(product1);
        repository.add(product2);
        repository.add(product3);

        Query<Product> base = repository.query().filter(p -> p.getPrice() > 30);
        Query<Product> sorted = repository.sortByPrice(base);
        Product[] result = sorted.getResult();

        assertEquals(2, result.length);
        assertEquals(product3, result[0]);
        assertEquals(product1, result[1]);
    }

    @Test
    void testSortByQuantityAscending() {
        repository.add(product1);
        repository.add(product2);
        repository.add(product3);

        Query<Product> sorted = repository.sortByQuantity();
        Product[] result = sorted.getResult();

        assertEquals(3, result.length);
        assertEquals(product3, result[0]);
        assertEquals(product1, result[1]);
        assertEquals(product2, result[2]);
    }

    @Test
    void testSortByQuantityDescending() {
        repository.add(product1);
        repository.add(product2);
        repository.add(product3);

        Query<Product> sorted = repository.sortByQuantity(SortOrder.DESC);
        Product[] result = sorted.getResult();

        assertEquals(3, result.length);
        assertEquals(product2, result[0]);
        assertEquals(product1, result[1]);
        assertEquals(product3, result[2]);
    }

    @Test
    void testSortByQuantityWithBaseQuery() {
        repository.add(product1);
        repository.add(product2);
        repository.add(product3);

        Query<Product> base = repository.query().filter(p -> p.getQuantity() >= 10);
        Query<Product> sorted = repository.sortByQuantity(base, SortOrder.DESC);
        Product[] result = sorted.getResult();

        assertEquals(2, result.length);
        assertEquals(product2, result[0]);
        assertEquals(product1, result[1]);
    }

    @Test
    void testSortByNameAscending() {
        repository.add(product1);
        repository.add(product2);
        repository.add(product3);

        Query<Product> sorted = repository.sortByName();
        Product[] result = sorted.getResult();

        assertEquals(3, result.length);
        assertEquals(product3, result[0]);
        assertEquals(product1, result[1]);
        assertEquals(product2, result[2]);
    }

    @Test
    void testSortByCreatedAtAscending() {
        repository.add(product1);
        repository.add(product2);
        repository.add(product3);

        Query<Product> sorted = repository.sortByCreatedAt();
        Product[] result = sorted.getResult();

        assertEquals(3, result.length);
        assertEquals(product1, result[0]);
        assertEquals(product2, result[1]);
        assertEquals(product3, result[2]);
    }

    @Test
    void testQuery() {
        repository.add(product1);
        repository.add(product2);
        repository.add(product3);

        Query<Product> query = repository.query();
        assertEquals(3, query.getSize());
    }

    @Test
    void testQueryFilter() {
        repository.add(product1);
        repository.add(product2);
        repository.add(product3);

        Query<Product> filtered = repository.query().filter(p -> p.getPrice() > 50);
        Product[] result = filtered.getResult();

        assertEquals(2, result.length);
        assertTrue(result[0].getPrice() > 50);
        assertTrue(result[1].getPrice() > 50);
    }

    @Test
    void testQueryPagination() {
        repository.add(product1);
        repository.add(product2);
        repository.add(product3);

        Query<Product> page = repository.query().paginate(1, 2);
        assertEquals(2, page.getSize());

        Query<Product> page2 = repository.query().paginate(2, 2);
        assertEquals(1, page2.getSize());
    }

    @Test
    void testEmptyRepository() {
        assertEquals(0, repository.getSize());
        Product[] all = repository.findAll();
        assertEquals(0, all.length);
    }

    @Test
    void testSortEmptyRepository() {
        Query<Product> sorted = repository.sortByPrice();
        assertEquals(0, sorted.getSize());
    }

    @Test
    void testQueryEmptyRepository() {
        Query<Product> query = repository.query();
        assertEquals(0, query.getSize());
    }

}