package test;

import static org.junit.jupiter.api.Assertions.*;

import main.lib.Query;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import java.util.Comparator;
import java.util.function.Predicate;

class QueryTest {

	@BeforeEach
	void setUp() {
	}

	@AfterEach
	void tearDown() {
	}

	@Test
	void testOfAndGetResult() {
		Integer[] data = { 1, 2, 3, 4, 5 };
		Query<Integer> query = Query.of(data);
		assertArrayEquals(data, query.getResult());
		assertEquals(5, query.getSize());
	}

	@Test
	void testFindWithPredicate() {
		String[] data = { "apple", "banana", "apricot", "cherry" };
		Query<String> query = Query.of(data);
		Predicate<String> startsWithA = s -> s.startsWith("a");
		Query<String> result = query.filter(startsWithA);
		assertArrayEquals(new String[] { "apple", "apricot" }, result.getResult());
		assertEquals(2, result.getSize());
	}

	@Test
	void testSortWithComparator() {
		Integer[] data = { 5, 3, 1, 4, 2 };
		Query<Integer> query = Query.of(data);
		Query<Integer> sorted = query.sort(Comparator.naturalOrder());
		assertArrayEquals(new Integer[] { 1, 2, 3, 4, 5 }, sorted.getResult());
	}

	@Test
	void testSkipZeroAndAll() {
		Integer[] data = { 10, 20, 30, 40 };
		Query<Integer> query = Query.of(data);
		assertArrayEquals(data, query.skip(0).getResult());
		assertArrayEquals(new Integer[] {}, query.skip(4).getResult());
	}

	@Test
	void testSkipPartial() {
		Integer[] data = { 10, 20, 30, 40, 50 };
		Query<Integer> query = Query.of(data);
		assertArrayEquals(new Integer[] { 20, 30, 40, 50 }, query.skip(1).getResult());
		assertArrayEquals(new Integer[] { 40, 50 }, query.skip(3).getResult());
	}

	@Test
	void testLimitZeroAndAll() {
		Integer[] data = { 1, 2, 3 };
		Query<Integer> query = Query.of(data);
		assertArrayEquals(new Integer[] {}, query.limit(0).getResult());
		assertArrayEquals(data, query.limit(3).getResult());
	}

	@Test
	void testLimitPartial() {
		Integer[] data = { 1, 2, 3, 4, 5 };
		Query<Integer> query = Query.of(data);
		assertArrayEquals(new Integer[] { 1, 2 }, query.limit(2).getResult());
	}

	@Test
	void testFindWithNoMatch() {
		Integer[] data = { 1, 2, 3 };
		Query<Integer> query = Query.of(data);
		Query<Integer> result = query.filter(x -> x > 10);
		assertEquals(0, result.getSize());
		assertArrayEquals(new Integer[] {}, result.getResult());
	}

	@Test
	void testSortWithCustomComparator() {
		String[] data = { "b", "a", "c" };
		Query<String> query = Query.of(data);
		Query<String> sorted = query.sort((s1, s2) -> s2.compareTo(s1));
		assertArrayEquals(new String[] { "c", "b", "a" }, sorted.getResult());
	}

	@Test
	void testSkipNegativeThrows() {
		Integer[] data = { 1, 2, 3 };
		Query<Integer> query = Query.of(data);
		assertThrows(IllegalArgumentException.class, () -> query.skip(-1));
	}

	@Test
	void testLimitNegativeThrows() {
		Integer[] data = { 1, 2, 3 };
		Query<Integer> query = Query.of(data);
		assertThrows(IllegalArgumentException.class, () -> query.limit(-5));
	}

	@Test
	void testSetSizeNegativeThrows() {
		Query<Integer> query = new Query<>();
		assertThrows(IllegalArgumentException.class, () -> query.setSize(-1));
	}

	@Test
	void testSetResultNullThrows() {
		Query<Integer> query = new Query<>();
		assertThrows(IllegalAccessException.class, () -> {
			query.getClass().getDeclaredMethod("setResult", Object[].class)
					.setAccessible(true);
			query.getClass().getDeclaredMethod("setResult", Object[].class)
					.invoke(query, (Object) null);
		});
	}

	@Test
	void testChainedOperations() {
		Integer[] data = { 5, 2, 8, 1, 3, 7 };
		Query<Integer> query = Query.of(data)
				.filter(x -> x > 2)
				.sort(Comparator.naturalOrder())
				.skip(1)
				.limit(2);

		assertArrayEquals(new Integer[] { 5, 7 }, query.getResult());
	}

	@Test
	void testMapAndForeachOperation() {
		Integer[] data = { 1, 2, 3, 4, 5 };
		Query<Integer> query = Query.of(data);
		query = query.map((value) -> (value * 2));
		query.forEach((value) -> {
			System.out.println(value);
		});
	}

	@Test
	void testFindReturnsFirstMatch() {
		String[] data = { "apple", "banana", "apricot", "cherry" };
		Query<String> query = Query.of(data);
		String found = query.find(s -> s.startsWith("a"));
		assertEquals("apple", found);
	}

	@Test
	void testFindReturnsNullIfNoMatch() {
		Integer[] data = { 1, 2, 3, 4 };
		Query<Integer> query = Query.of(data);
		Integer found = query.find(x -> x > 10);
		assertNull(found);
	}

	@Test
	void testFindWithMultipleMatchesReturnsFirst() {
		Integer[] data = { 2, 4, 6, 8 };
		Query<Integer> query = Query.of(data);
		Integer found = query.find(x -> x % 2 == 0);
		assertEquals(2, found);
	}

	@Test
	void testFindWithEmptyArrayReturnsNull() {
		Integer[] data = {};
		Query<Integer> query = Query.of(data);
		Integer found = query.find(x -> true);
		assertNull(found);
	}
}
