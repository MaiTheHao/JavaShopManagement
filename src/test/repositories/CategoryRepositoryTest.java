package test.repositories;

import static org.junit.jupiter.api.Assertions.*;
import main.models.Category;
import main.repositories.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import java.time.LocalDateTime;

class CategoryRepositoryTest {

    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        categoryRepository = CategoryRepository.getInstance();
        categoryRepository.clear();
    }

    @AfterEach
    void tearDown() {
        categoryRepository = null;
    }

    @Test
    void shouldReturnSingletonInstance() {
        CategoryRepository repo1 = CategoryRepository.getInstance();
        CategoryRepository repo2 = CategoryRepository.getInstance();
        assertSame(repo1, repo2);
    }

    @Test
    void shouldReturnSingletonInstanceWithCapacity() {
        CategoryRepository repo1 = CategoryRepository.getInstance(100);
        CategoryRepository repo2 = CategoryRepository.getInstance(200);
        assertSame(repo1, repo2);
    }

    @Test
    void shouldAddCategorySuccessfully() {
        Category category = new Category("Electronics");
        category.setId(1);

        categoryRepository.add(category);

        assertEquals(1, categoryRepository.getSize());
        assertEquals(category, categoryRepository.findById(1));
    }

    @Test
    void shouldUpdateCategorySuccessfully() {
        Category category = new Category("Electronics");
        category.setId(1);
        categoryRepository.add(category);

        Category updatedCategory = new Category("Updated Electronics");
        updatedCategory.setId(1);

        categoryRepository.update(updatedCategory);

        assertEquals("Updated Electronics", categoryRepository.findById(1).getName());
    }

    @Test
    void shouldRemoveCategorySuccessfully() {
        Category category1 = new Category("Electronics");
        category1.setId(1);
        Category category2 = new Category("Books");
        category2.setId(2);

        categoryRepository.add(category1);
        categoryRepository.add(category2);

        categoryRepository.remove(1);

        assertEquals(1, categoryRepository.getSize());
        assertNull(categoryRepository.findById(1));
        assertNotNull(categoryRepository.findById(2));
    }

    @Test
    void shouldFindCategoryById() {
        Category category = new Category("Sports");
        category.setId(10);
        categoryRepository.add(category);

        Category found = categoryRepository.findById(10);
        assertEquals(category, found);
    }

    @Test
    void shouldReturnNullWhenCategoryNotFoundById() {
        Category found = categoryRepository.findById(999);
        assertNull(found);
    }

    @Test
    void shouldFindCategoryByName() {
        Category category = new Category("Home & Garden");
        category.setId(1);
        categoryRepository.add(category);

        Category found = categoryRepository.findByName("Home & Garden");
        assertEquals(category, found);
    }

    @Test
    void shouldFindCategoryByNameCaseInsensitive() {
        Category category = new Category("Fashion");
        category.setId(1);
        categoryRepository.add(category);

        Category found = categoryRepository.findByName("FASHION");
        assertEquals(category, found);
    }

    @Test
    void shouldCheckIfCategoryExists() {
        Category category = new Category("Technology");
        category.setId(5);
        categoryRepository.add(category);

        assertTrue(categoryRepository.exists(5));
        assertFalse(categoryRepository.exists(99));
    }

    @Test
    void shouldSortCategoriesByName() {
        Category category1 = new Category("Zebra");
        category1.setId(1);
        Category category2 = new Category("Apple");
        category2.setId(2);
        Category category3 = new Category("Banana");
        category3.setId(3);

        categoryRepository.add(category1);
        categoryRepository.add(category2);
        categoryRepository.add(category3);

        Category[] sorted = categoryRepository.sortByName().getResult();

        assertEquals("Apple", sorted[0].getName());
        assertEquals("Banana", sorted[1].getName());
        assertEquals("Zebra", sorted[2].getName());
    }

    @Test
    void shouldSortCategoriesByCreatedAt() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime earlier = now.minusHours(1);
        LocalDateTime latest = now.minusMinutes(30);

        Category category1 = new Category("First");
        category1.setId(1);
        category1.setCreatedAt(now);

        Category category2 = new Category("Second");
        category2.setId(2);
        category2.setCreatedAt(earlier);

        Category category3 = new Category("Third");
        category3.setId(3);
        category3.setCreatedAt(latest);

        categoryRepository.add(category1);
        categoryRepository.add(category2);
        categoryRepository.add(category3);

        Category[] sorted = categoryRepository.sortByCreatedAt().getResult();

        assertEquals("Second", sorted[0].getName());
        assertEquals("Third", sorted[1].getName());
        assertEquals("First", sorted[2].getName());
    }

    @Test
    void shouldHandleEmptyRepository() {
        assertEquals(0, categoryRepository.getSize());
        assertNull(categoryRepository.findById(1));
        assertNull(categoryRepository.findByName("Any"));
        assertEquals(0, categoryRepository.query().getSize());
    }

    @Test
    void shouldClearRepository() {
        Category category1 = new Category("Category1");
        category1.setId(1);
        Category category2 = new Category("Category2");
        category2.setId(2);

        categoryRepository.add(category1);
        categoryRepository.add(category2);

        assertEquals(2, categoryRepository.getSize());

        categoryRepository.clear();

        assertEquals(0, categoryRepository.getSize());
    }

    @Test
    void shouldWorkWithQueryOperations() {
        Category category1 = new Category("Electronics");
        category1.setId(1);
        Category category2 = new Category("Books");
        category2.setId(2);
        Category category3 = new Category("Electronic Gadgets");
        category3.setId(3);

        categoryRepository.add(category1);
        categoryRepository.add(category2);
        categoryRepository.add(category3);

        Category[] filtered = categoryRepository.query()
                .filter(c -> c.getName().toLowerCase().contains("electronic"))
                .getResult();

        assertEquals(2, filtered.length);
    }
}
