package test.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import main.repositories.CategoryRepository;
import main.models.Category;
import main.enumerations.SortOrder;
import main.errors.BadRequestException;
import main.errors.NotFoundException;
import main.utils.Query;

import java.time.LocalDateTime;
import java.lang.reflect.Field;

public class CategoryRepositoryTest {

    private CategoryRepository repository;
    private Category category1;
    private Category category2;
    private Category category3;

    @BeforeEach
    void setUp() throws Exception {
        Field instanceField = CategoryRepository.class.getDeclaredField("instance");
        instanceField.setAccessible(true);
        instanceField.set(null, null);

        repository = CategoryRepository.getInstance();
        repository.clear();

        category1 = new Category(1L, "Electronics", "Electronic devices and gadgets");
        category1.setCreatedAt(LocalDateTime.now().minusDays(3));

        category2 = new Category(2L, "Books", "Books and literature");
        category2.setCreatedAt(LocalDateTime.now().minusDays(2));

        category3 = new Category(3L, "Clothing", "Apparel and accessories");
        category3.setCreatedAt(LocalDateTime.now().minusDays(1));
    }

    @Test
    @DisplayName("Should return same instance (Singleton pattern)")
    void testSingletonPattern() {
        CategoryRepository instance1 = CategoryRepository.getInstance();
        CategoryRepository instance2 = CategoryRepository.getInstance();

        assertSame(instance1, instance2, "Should return the same instance");
    }

    @Test
    @DisplayName("Should create instance with custom capacity")
    void testSingletonWithCapacity() throws Exception {
        Field instanceField = CategoryRepository.class.getDeclaredField("instance");
        instanceField.setAccessible(true);
        instanceField.set(null, null);

        CategoryRepository customRepository = CategoryRepository.getInstance(10);
        assertEquals(10, customRepository.getCapacity(), "Should have custom capacity");
    }

    @Test
    @DisplayName("Should add category successfully")
    void testAddCategory() {
        repository.add(category1);

        assertEquals(1, repository.getSize(), "Size should be 1");
        assertEquals(category1, repository.findById(1L), "Should find added category");
    }

    @Test
    @DisplayName("Should throw exception when adding null category")
    void testAddNullCategory() {
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> repository.add(null));
        assertEquals("Data to add cannot be null.", exception.getMessage());
    }

    @Test
    @DisplayName("Should update category successfully")
    void testUpdateCategory() {
        repository.add(category1);

        Category updatedCategory = new Category(1L, "Updated Electronics", "Updated description");
        repository.update(updatedCategory);

        Category found = repository.findById(1L);
        assertEquals("Updated Electronics", found.getName());
        assertEquals("Updated description", found.getDescription());
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent category")
    void testUpdateNonExistentCategory() {
        Category nonExistent = new Category(999L, "Non-existent", "Description");

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> repository.update(nonExistent));
        assertEquals("Entity with ID 999 not found.", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when updating null category")
    void testUpdateNullCategory() {
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> repository.update(null));
        assertEquals("Data to update cannot be null.", exception.getMessage());
    }

    @Test
    @DisplayName("Should remove category successfully")
    void testRemoveCategory() {
        repository.add(category1);
        repository.add(category2);

        repository.remove(1L);

        assertEquals(1, repository.getSize(), "Size should be 1 after removal");
        assertThrows(NotFoundException.class, () -> repository.findById(1L));
        assertDoesNotThrow(() -> repository.findById(2L));
    }

    @Test
    @DisplayName("Should throw exception when removing non-existent category")
    void testRemoveNonExistentCategory() {
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> repository.remove(999L));
        assertEquals("Entity with ID 999 not found.", exception.getMessage());
    }

    @Test
    @DisplayName("Should clear all categories")
    void testClearCategories() {
        repository.add(category1);
        repository.add(category2);
        repository.add(category3);

        repository.clear();

        assertEquals(0, repository.getSize(), "Size should be 0 after clear");
    }

    @Test
    @DisplayName("Should find category by ID")
    void testFindById() {
        repository.add(category1);

        Category found = repository.findById(1L);

        assertNotNull(found);
        assertEquals(category1.getId(), found.getId());
        assertEquals(category1.getName(), found.getName());
    }

    @Test
    @DisplayName("Should throw exception when finding non-existent ID")
    void testFindByNonExistentId() {
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> repository.findById(999L));
        assertEquals("Entity with ID 999 not found.", exception.getMessage());
    }

    @Test
    @DisplayName("Should find category by name")
    void testFindByName() {
        repository.add(category1);

        Category found = repository.findByName("Electronics");

        assertNotNull(found);
        assertEquals(category1.getName(), found.getName());
    }

    @Test
    @DisplayName("Should find category by name (case insensitive)")
    void testFindByNameCaseInsensitive() {
        repository.add(category1);

        Category found = repository.findByName("electronics");

        assertNotNull(found);
        assertEquals(category1.getName(), found.getName());
    }

    @Test
    @DisplayName("Should throw exception when finding by null name")
    void testFindByNullName() {
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> repository.findByName(null));
        assertEquals("Name cannot be null or empty.", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when finding by empty name")
    void testFindByEmptyName() {
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> repository.findByName("   "));
        assertEquals("Name cannot be null or empty.", exception.getMessage());
    }

    @Test
    @DisplayName("Should return all categories")
    void testFindAll() {
        repository.add(category1);
        repository.add(category2);

        Category[] all = repository.findAll();

        assertEquals(2, all.length);
        assertEquals(category1, all[0]);
        assertEquals(category2, all[1]);
    }

    @Test
    @DisplayName("Should check if category exists by ID")
    void testExists() {
        repository.add(category1);

        assertTrue(repository.exists(1L), "Category should exist");
        assertFalse(repository.exists(999L), "Category should not exist");
    }

    @Test
    @DisplayName("Should check if category exists by entity")
    void testExistsByEntity() {
        repository.add(category1);

        assertTrue(repository.exists(category1), "Category should exist");
        assertFalse(repository.exists(category2), "Category should not exist");
    }

    @Test
    @DisplayName("Should check for ID duplication")
    void testIdDuplicated() {
        repository.add(category1);

        assertTrue(repository.idDuplicated(1L), "ID should be duplicated");
        assertFalse(repository.idDuplicated(999L), "ID should not be duplicated");
    }

    @Test
    @DisplayName("Should check if data is duplicated")
    void testIsDuplicated() {
        repository.add(category1);

        assertTrue(repository.isDuplicated(category1), "Category should be duplicated");
        assertFalse(repository.isDuplicated(category2), "Category should not be duplicated");
        assertFalse(repository.isDuplicated(null), "Null should not be duplicated");
    }

    @Test
    @DisplayName("Should sort by name in ascending order")
    void testSortByNameAscending() {
        repository.add(category3);
        repository.add(category1);
        repository.add(category2);

        Query<Category> sorted = repository.sortByName();
        Category[] result = sorted.getResult();

        assertEquals("Books", result[0].getName());
        assertEquals("Clothing", result[1].getName());
        assertEquals("Electronics", result[2].getName());
    }

    @Test
    @DisplayName("Should sort by name in descending order")
    void testSortByNameDescending() {
        repository.add(category3);
        repository.add(category1);
        repository.add(category2);

        Query<Category> sorted = repository.sortByName(SortOrder.DESC);
        Category[] result = sorted.getResult();

        assertEquals("Electronics", result[0].getName());
        assertEquals("Clothing", result[1].getName());
        assertEquals("Books", result[2].getName());
    }

    @Test
    @DisplayName("Should sort by created date in ascending order")
    void testSortByCreatedAtAscending() {
        repository.add(category1);
        repository.add(category3);
        repository.add(category2);

        Query<Category> sorted = repository.sortByCreatedAt();
        Category[] result = sorted.getResult();

        assertEquals(category1, result[0]);
        assertEquals(category2, result[1]);
        assertEquals(category3, result[2]);
    }

    @Test
    @DisplayName("Should sort by created date in descending order")
    void testSortByCreatedAtDescending() {
        repository.add(category1);
        repository.add(category3);
        repository.add(category2);

        Query<Category> sorted = repository.sortByCreatedAt(SortOrder.DESC);
        Category[] result = sorted.getResult();

        assertEquals(category3, result[0]);
        assertEquals(category2, result[1]);
        assertEquals(category1, result[2]);
    }

    @Test
    @DisplayName("Should check capacity management")
    void testCapacityManagement() {
        assertEquals(5, repository.getCapacity());

        for (int i = 1; i <= 6; i++) {
            repository.add(new Category((long) i, "Category" + i, "Description" + i));
        }

        assertEquals(6, repository.getSize());
        assertTrue(repository.getCapacity() >= 6, "Capacity should be expanded");
    }

    @Test
    @DisplayName("Should handle query operations")
    void testQueryOperations() {
        repository.add(category1);
        repository.add(category2);
        repository.add(category3);

        Query<Category> query = repository.query();
        assertNotNull(query);

        Category found = query.find(c -> c.getName().equals("Books"));
        assertEquals(category2, found);

        Category[] filtered = query.filter(c -> c.getName().startsWith("C")).getResult();
        assertEquals(1, filtered.length);
        assertEquals(category3, filtered[0]);
    }
}