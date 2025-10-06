package main.utils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import main.errors.BadRequestException;
import main.errors.InternalAppException;

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
			throw new BadRequestException("Size must be greater than 0");
		this.size = size;
	}

	private void setResult(T[] data) {
		if (data == null)
			throw new BadRequestException("Data array cannot be null");
		this.result = data;
		setSize(data.length);
	}

	public static <T> Query<T> of(T[] data) {
		Query<T> query = new Query<>();
		query.setResult(data);
		return query;
	}

	// ================= Intermediate Operations =================

	public Query<T> filter(Predicate<T> callback) {
		try {
			T[] result = this.getResult();
			int count = 0;
			for (int i = 0; i < this.size; i++) {
				if (callback.test(this.result[i])) {
					result[count++] = this.result[i];
				}
			}
			return Query.of(Arrays.copyOf(result, count));
		} catch (Exception e) {
			throw InternalAppException.from(e);
		}
	}

	public Query<T> sort(Comparator<T> comparator) {
		try {
			T[] sorted = this.getResult();
			boolean swapped;

			for (int i = 0; i < this.size - 1; i++) {
				swapped = false;
				for (int j = 0; j < this.size - i - 1; j++) {
					if (comparator.compare(sorted[j], sorted[j + 1]) > 0) {
						T temp = sorted[j];
						sorted[j] = sorted[j + 1];
						sorted[j + 1] = temp;
						swapped = true;
					}
				}

				if (!swapped) {
					break;
				}
			}

			return Query.of(sorted);
		} catch (Exception e) {
			throw InternalAppException.from(e);
		}
	}

	public Query<T> skip(int n) {
		if (n < 0)
			throw new BadRequestException("Skip count must be non-negative");
		if (n == 0)
			return Query.of(this.result);
		if (n >= this.size)
			return Query.of(Arrays.copyOf(this.result, 0));
		T[] result = Arrays.copyOfRange(this.result, n, this.size);
		return Query.of(result);
	}

	public Query<T> limit(int n) {
		if (n < 0)
			throw new BadRequestException("Limit must be non-negative");
		if (n >= this.size)
			return Query.of(this.result);
		T[] result = Arrays.copyOfRange(this.result, 0, n);
		return Query.of(result);
	}

	public Query<T> paginate(int page, int limit) {
		if (page < 1)
			throw new BadRequestException("Page number must be greater than 0");
		if (limit < 1)
			throw new BadRequestException("Limit must be greater than 0");

		int start = (page - 1) * limit;
		if (start >= this.size)
			return Query.of(Arrays.copyOf(this.result, 0));

		int end = Math.min(start + limit, this.size);
		T[] result = Arrays.copyOfRange(this.result, start, end);
		return Query.of(result);
	}

	public Query<T> map(UnaryOperator<T> callback) {
		try {
			T[] copied = this.getResult();
			for (int i = 0; i < this.getSize(); i++)
				copied[i] = callback.apply(copied[i]);
			return Query.of(copied);
		} catch (Exception e) {
			throw InternalAppException.from(e);
		}
	}

	// ================= Terminal Operations =================

	public T find(Predicate<T> callback) {
		try {
			T[] result = this.getResult();
			for (int i = 0; i < this.size; i++) {
				if (callback.test(this.result[i]))
					return result[i];
			}
			return null;
		} catch (Exception e) {
			throw InternalAppException.from(e);
		}
	}

	public int indexOf(Predicate<T> callback) {
		try {
			for (int i = 0; i < this.size; i++) {
				if (callback.test(this.result[i]))
					return i;
			}
			return -1;
		} catch (Exception e) {
			throw InternalAppException.from(e);
		}
	}

	public void forEach(Consumer<T> callback) {
		try {
			T[] copied = this.getResult();
			for (int i = 0; i < this.getSize(); i++)
				callback.accept(copied[i]);
		} catch (Exception e) {
			throw InternalAppException.from(e);
		}
	}
}
