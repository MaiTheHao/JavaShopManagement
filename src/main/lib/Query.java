package main.lib;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class Query<T> {
	private int size;
	private T[] result;

	public Query() {
	}

	public int getSize() {
		return size;
	}

	public T[] getResult() {
		return Arrays.copyOf(this.result, this.size);
	}

	public void setSize(int size) {
		if (size < 0)
			throw new IllegalArgumentException("Size must be greater than 0");
		this.size = size;
	}

	private void setResult(T[] data) {
		if (data == null)
			throw new IllegalArgumentException("Data array cannot be null");
		this.result = data;
		setSize(data.length);
	}

	public static <T> Query<T> of(T[] data) {
		Query<T> query = new Query<>();
		query.setResult(data);
		return query;
	}

	public Query<T> filter(Predicate<T> callback) {
		T[] result = this.getResult();
		int count = 0;
		for (int i = 0; i < this.size; i++) {
			if (callback.test(this.result[i])) {
				result[count++] = this.result[i];
			}
		}
		return Query.of(Arrays.copyOf(result, count));
	}

	public Query<T> sort(Comparator<T> comparator) {
		T[] sorted = this.getResult();
		Arrays.sort(sorted, comparator);
		return Query.of(sorted);
	}

	public Query<T> skip(int n) {
		if (n < 0)
			throw new IllegalArgumentException("Skip count must be non-negative");
		if (n == 0)
			return Query.of(this.result);
		if (n >= this.size)
			return Query.of(Arrays.copyOf(this.result, 0));
		T[] result = Arrays.copyOfRange(this.result, n, this.size);
		return Query.of(result);
	}

	public Query<T> limit(int n) {
		if (n < 0)
			throw new IllegalArgumentException("Limit must be non-negative");
		if (n >= this.size)
			return Query.of(this.result);
		T[] result = Arrays.copyOfRange(this.result, 0, n);
		return Query.of(result);
	}

	public T find(Predicate<T> callback) {
		T[] result = this.getResult();
		for (int i = 0; i < this.size; i++) {
			if (callback.test(this.result[i]))
				return result[i];
		}
		return null;
	}

	public int indexOf(Predicate<T> callback) {
		for (int i = 0; i < this.size; i++) {
			if (callback.test(this.result[i]))
				return i;
		}
		return -1;
	}

	public Query<T> map(UnaryOperator<T> callback) {
		T[] copied = this.getResult();
		for (int i = 0; i < this.getSize(); i++)
			copied[i] = callback.apply(copied[i]);
		return Query.of(copied);
	}

	public void forEach(Consumer<T> callback) {
		T[] copied = this.getResult();
		for (int i = 0; i < this.getSize(); i++)
			callback.accept(copied[i]);
	}
}
