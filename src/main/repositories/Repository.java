package main.repositories;

import main.lib.Query;
import main.models.Entity;
import main.enumerations.SortOrder;

public abstract class Repository<T extends Entity> {
	protected T[] datas;
	protected int size;
	protected int capacity;

	public void add(T data) {
		if (size < capacity) {
			datas[size++] = data;
		} else {
			throw new IllegalStateException("Repository is full");
		}
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
		for (int i = 0; i < size; i++) {
			if (this.datas[i] != null && this.datas[i].getId() == id) {
				for (int j = i; j < size - 1; i++) {
					datas[j] = datas[j + 1];
				}
				datas[size - 1] = null;
				size--;
				return;
			}
		}
	}

	public Query<T> query() {
		return Query.of(this.datas);

	}

	public Query<T> findAll() {
		return query();
	}

	public T findById(int id) {
		return null;

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

}