package main.abstracts.repositories;

import main.models.Entity;
import main.utils.Query;
import java.util.Arrays;
import main.enumerations.SortOrder;
import main.errors.AppException;
import main.errors.BadRequestException;
import main.errors.InternalAppException;
import main.errors.NotFoundException;

public abstract class Repository<T extends Entity> {
	protected static final int DEFAULT_CAPACITY = 5;
	protected T[] datas;
	protected int size;
	protected int capacity;

	public int getSize() {
		return size;
	}

	public int getCapacity() {
		return capacity;
	}

	public void add(T data) {
		try {
			if (data == null)
				throw new BadRequestException("Data to add cannot be null.");

			if (isOutOfCapacity())
				increaseCapacity();

			datas[size++] = data;

		} catch (AppException e) {
			throw e;
		} catch (Exception e) {
			throw new InternalAppException("Failed to add data: " + e.getMessage());
		}
	}

	public void update(T data) {
		if (data == null)
			throw new BadRequestException("Data to update cannot be null.");

		boolean updated = false;
		int index = this.query().indexOf(e -> e.getId() == data.getId());
		if (index != -1) {
			this.datas[index] = data;
			updated = true;
		}

		if (!updated)
			throw new NotFoundException("Entity with ID " + data.getId() + " not found.");
	}

	public void remove(Long id) {
		int index = this.query().indexOf(e -> e.getId().equals(id));
		if (index == -1)
			throw new NotFoundException("Entity with ID " + id + " not found.");

		System.arraycopy(datas, index + 1, datas, index, size - index - 1);
		datas[--size] = null;
	}

	public void clear() {
		for (int i = 0; i < this.size; i++) {
			datas[i] = null;
		}
		this.size = 0;
	}

	public Query<T> query() {
		return Query.of(Arrays.copyOf(this.datas, this.size));
	}

	public T[] findAll() {
		return Arrays.copyOf(this.datas, this.size);
	}

	public T findById(Long id) {
		T result = this.query().find(e -> e.getId().equals(id));

		if (result == null)
			throw new NotFoundException("Entity with ID " + id + " not found.");

		return result;
	}

	public T findByName(String name) {
		if (name == null || name.trim().isEmpty())
			throw new BadRequestException("Name cannot be null or empty.");

		T result = this.query().find(e -> (e.getName().trim().compareToIgnoreCase(name) == 0));

		if (result == null)
			throw new NotFoundException("Entity with name '" + name + "' not found.");

		return result;
	}

	public Query<T> sortByName() {
		return sortByName(SortOrder.ASC);
	}

	public Query<T> sortByName(SortOrder order) {
		int flag = (order == SortOrder.DESC) ? -1 : 1;
		return this.query().sort((e1, e2) -> (e1.getName().trim().compareToIgnoreCase(e2.getName().trim()) * flag));
	}

	public Query<T> sortByName(Query<T> base) {
		return sortByName(base, SortOrder.ASC);
	}

	public Query<T> sortByName(Query<T> base, SortOrder order) {
		int flag = (order == SortOrder.DESC) ? -1 : 1;
		return base.sort((e1, e2) -> (e1.getName().trim().compareToIgnoreCase(e2.getName().trim()) * flag));
	}

	public Query<T> sortByCreatedAt() {
		return sortByCreatedAt(SortOrder.ASC);
	}

	public Query<T> sortByCreatedAt(SortOrder order) {
		int flag = (order == SortOrder.DESC) ? -1 : 1;
		return this.query().sort((e1, e2) -> e1.getCreatedAt().compareTo(e2.getCreatedAt()) * flag);
	}

	public Query<T> sortByCreatedAt(Query<T> base) {
		return sortByCreatedAt(base, SortOrder.ASC);
	}

	public Query<T> sortByCreatedAt(Query<T> base, SortOrder order) {
		int flag = (order == SortOrder.DESC) ? -1 : 1;
		return base.sort((e1, e2) -> e1.getCreatedAt().compareTo(e2.getCreatedAt()) * flag);
	}

	public boolean exists(Long id) {
		return this.query().find(e -> e.getId().equals(id)) != null;
	}

	public boolean exists(Entity entity) {
		return this.exists(entity.getId());
	}

	public boolean idDuplicated(Long id) {
		return this.query().find(e -> e.getId().equals(id)) != null;
	}

	public boolean isDuplicated(T data) {
		if (data == null)
			return false;

		return this.idDuplicated(data.getId());
	}

	public boolean isOutOfCapacity() {
		return this.size >= this.capacity;
	}

	protected void increaseCapacity() {
		try {
			int newCapacity = Math.max(1, capacity * 2);
			datas = Arrays.copyOf(datas, newCapacity);
			capacity = newCapacity;
		} catch (OutOfMemoryError e) {
			throw new InternalAppException("Cannot increase capacity: " + e.getMessage());
		}
	}
}