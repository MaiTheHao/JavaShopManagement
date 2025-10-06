package test.lib;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import main.errors.BadRequestException;
import main.utils.Query;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

class QueryTest {

	@BeforeEach
	void setUp() {
	}

	@AfterEach
	void tearDown() {
	}

	@Test
	void shouldCreateQueryAndReturnCorrectSizeAndData() {
		Integer[] data = { 1, 2, 3, 4, 5 };
		Query<Integer> query = Query.of(data);
		assertArrayEquals(data, query.getResult());
		assertEquals(5, query.getSize());
	}

	@Test
	void shouldFilterElementsMatchingPredicate() {
		String[] data = { "apple", "banana", "apricot", "cherry" };
		Query<String> query = Query.of(data);
		Predicate<String> startsWithA = s -> s.startsWith("a");
		Query<String> result = query.filter(startsWithA);
		assertArrayEquals(new String[] { "apple", "apricot" }, result.getResult());
		assertEquals(2, result.getSize());
	}

	@Test
	void shouldSortElementsUsingComparator() {
		Integer[] data = { 5, 3, 1, 4, 2 };
		Query<Integer> query = Query.of(data);
		Query<Integer> sorted = query.sort(Comparator.naturalOrder());
		assertArrayEquals(new Integer[] { 1, 2, 3, 4, 5 }, sorted.getResult());
	}

	@Test
	void shouldSkipZeroElementsAndAllElements() {
		Integer[] data = { 10, 20, 30, 40 };
		Query<Integer> query = Query.of(data);
		assertArrayEquals(data, query.skip(0).getResult());
		assertArrayEquals(new Integer[] {}, query.skip(4).getResult());
	}

	@Test
	void shouldSkipSpecifiedNumberOfElements() {
		Integer[] data = { 10, 20, 30, 40, 50 };
		Query<Integer> query = Query.of(data);
		assertArrayEquals(new Integer[] { 20, 30, 40, 50 }, query.skip(1).getResult());
		assertArrayEquals(new Integer[] { 40, 50 }, query.skip(3).getResult());
	}

	@Test
	void shouldLimitToZeroElementsAndAllElements() {
		Integer[] data = { 1, 2, 3 };
		Query<Integer> query = Query.of(data);
		assertArrayEquals(new Integer[] {}, query.limit(0).getResult());
		assertArrayEquals(data, query.limit(3).getResult());
		assertArrayEquals(data, query.limit(10).getResult());
	}

	@Test
	void shouldLimitToSpecifiedNumberOfElements() {
		Integer[] data = { 1, 2, 3, 4, 5 };
		Query<Integer> query = Query.of(data);
		assertArrayEquals(new Integer[] { 1, 2 }, query.limit(2).getResult());
	}

	@Test
	void shouldReturnEmptyResultWhenNoElementsMatch() {
		Integer[] data = { 1, 2, 3 };
		Query<Integer> query = Query.of(data);
		Query<Integer> result = query.filter(x -> x > 10);
		assertEquals(0, result.getSize());
		assertArrayEquals(new Integer[] {}, result.getResult());
	}

	@Test
	void shouldSortUsingCustomComparator() {
		String[] data = { "b", "a", "c" };
		Query<String> query = Query.of(data);
		Query<String> sorted = query.sort((s1, s2) -> s2.compareTo(s1));
		assertArrayEquals(new String[] { "c", "b", "a" }, sorted.getResult());
	}

	@Test
	void shouldThrowExceptionWhenSkippingNegativeElements() {
		Integer[] data = { 1, 2, 3 };
		Query<Integer> query = Query.of(data);
		BadRequestException exception = assertThrows(BadRequestException.class, () -> query.skip(-1));
		assertEquals("Skip count must be non-negative", exception.getMessage());
	}

	@Test
	void shouldThrowExceptionWhenLimitIsNegative() {
		Integer[] data = { 1, 2, 3 };
		Query<Integer> query = Query.of(data);
		BadRequestException exception = assertThrows(BadRequestException.class, () -> query.limit(-5));
		assertEquals("Limit must be non-negative", exception.getMessage());
	}

	@Test
	void shouldThrowExceptionWhenSetSizeIsNegative() {
		Query<Integer> query = new Query<>();
		BadRequestException exception = assertThrows(BadRequestException.class, () -> query.setSize(-1));
		assertEquals("Size must be greater than 0", exception.getMessage());
	}

	@Test
	void shouldChainMultipleOperationsCorrectly() {
		Integer[] data = { 5, 2, 8, 1, 3, 7 };
		Query<Integer> query = Query.of(data)
				.filter(x -> x > 2)
				.sort(Comparator.naturalOrder())
				.skip(1)
				.limit(2);

		assertArrayEquals(new Integer[] { 5, 7 }, query.getResult());
	}

	@Test
	void shouldMapElementsUsingFunction() {
		Integer[] data = { 1, 2, 3, 4, 5 };
		Query<Integer> query = Query.of(data);
		Query<Integer> mapped = query.map(value -> value * 2);
		assertArrayEquals(new Integer[] { 2, 4, 6, 8, 10 }, mapped.getResult());
	}

	@Test
	void shouldExecuteForEachOnAllElements() {
		Integer[] data = { 1, 2, 3, 4, 5 };
		Query<Integer> query = Query.of(data);
		AtomicInteger sum = new AtomicInteger(0);
		query.forEach(sum::addAndGet);
		assertEquals(15, sum.get());
	}

	@Test
	void shouldFindFirstMatchingElement() {
		String[] data = { "apple", "banana", "apricot", "cherry" };
		Query<String> query = Query.of(data);
		String found = query.find(s -> s.startsWith("a"));
		assertEquals("apple", found);
	}

	@Test
	void shouldReturnNullWhenNoElementMatches() {
		Integer[] data = { 1, 2, 3, 4 };
		Query<Integer> query = Query.of(data);
		Integer found = query.find(x -> x > 10);
		assertNull(found);
	}

	@Test
	void shouldReturnFirstElementWhenMultipleElementsMatch() {
		Integer[] data = { 2, 4, 6, 8 };
		Query<Integer> query = Query.of(data);
		Integer found = query.find(x -> x % 2 == 0);
		assertEquals(2, found);
	}

	@Test
	void shouldReturnNullWhenFindingInEmptyArray() {
		Integer[] data = {};
		Query<Integer> query = Query.of(data);
		Integer found = query.find(x -> true);
		assertNull(found);
	}

	@Test
	void shouldHandleNullElementsInArray() {
		String[] data = { "apple", null, "banana", null };
		Query<String> query = Query.of(data);
		assertEquals(4, query.getSize());
		assertArrayEquals(data, query.getResult());
	}

	@Test
	void shouldFilterNullElements() {
		String[] data = { "apple", null, "banana", null };
		Query<String> query = Query.of(data);
		Query<String> filtered = query.filter(x -> x != null);
		assertArrayEquals(new String[] { "apple", "banana" }, filtered.getResult());
		assertEquals(2, filtered.getSize());
	}

	@Test
	void shouldThrowExceptionWhenSkippingMoreThanSize() {
		Integer[] data = { 1, 2, 3 };
		Query<Integer> query = Query.of(data);
		Query<Integer> result = query.skip(5);
		assertArrayEquals(new Integer[] {}, result.getResult());
		assertEquals(0, result.getSize());
	}

	@Test
	void shouldCreateEmptyQueryFromEmptyArray() {
		Integer[] data = {};
		Query<Integer> query = Query.of(data);
		assertEquals(0, query.getSize());
		assertArrayEquals(new Integer[] {}, query.getResult());
	}

	@Test
	void shouldMaintainImmutabilityOfOriginalArray() {
		Integer[] data = { 1, 2, 3, 4, 5 };
		Query<Integer> query = Query.of(data);
		Query<Integer> mapped = query.map(x -> x * 10);

		assertArrayEquals(new Integer[] { 1, 2, 3, 4, 5 }, query.getResult());
		assertArrayEquals(new Integer[] { 10, 20, 30, 40, 50 }, mapped.getResult());
	}
}
