package main.repositories;

import main.lib.Query;
import main.models.Entity;

import java.util.Arrays;

import main.enumerations.SortOrder;

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
		if (isOutOfCapacity()) {
			increaseCapacity();
		}

		datas[size++] = data;
	}

	public void update(T data) {
		if (data == null) {
			return;
		}
		for (int i = 0; i < size; i++) {
			if (this.datas[i] != null && this.datas[i].getId() == data.getId()) {
				this.datas[i] = data;
			}
		}
	}

	public void remove(int id) {
		int index = this.query().indexOf(e -> e.getId() == id);
		if (index != -1) {
			System.arraycopy(datas, index + 1, datas, index, size - index - 1);
			datas[size - 1] = null;
			size--;
		}
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

	public Query<T> findAll() {
		return query();
	}

	public T findById(int id) {
		return this.query().find(e -> e.getId() == id);
	}

	public T findByName(String name) {
		return this.query().find(e -> (e.getName().trim().compareToIgnoreCase(name) == 0));
	}

	public Query<T> sortByName() {
		return this.query().sort((e1, e2) -> e1.getName().trim().compareToIgnoreCase(e2.getName().trim()));
	}

	public Query<T> sortByName(SortOrder order) {
		int flag = (order == SortOrder.DESC) ? -1 : 1;
		return this.query().sort((e1, e2) -> (e1.getName().trim().compareToIgnoreCase(e2.getName().trim()) * flag));
	}

	public Query<T> sortByCreatedAt() {
		return this.query().sort((e1, e2) -> e1.getCreatedAt().compareTo(e2.getCreatedAt()));
	}

	public Query<T> sortByCreatedAt(SortOrder order) {
		int flag = (order == SortOrder.DESC) ? -1 : 1;
		return this.query().sort((e1, e2) -> e1.getCreatedAt().compareTo(e2.getCreatedAt()) * flag);
	}

	public boolean exists(int id) {
		return this.query().find(e -> e.getId() == id) != null;
	}

	public boolean exists(Entity entity) {
		return this.exists(entity.getId());
	}

	public boolean idDuplicated(int id) {
		return this.query().find(e -> e.getId() == id) != null;
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
		int newCapacity = Math.max(1, capacity * 2);
		datas = Arrays.copyOf(datas, newCapacity);
		capacity = newCapacity;
	}
}