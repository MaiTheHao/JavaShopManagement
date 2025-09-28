package main.repositories;

import main.models.Category;

public class CategoryRepository extends Repository<Category> {
	private static CategoryRepository instance;

	private CategoryRepository() {
	}

	private CategoryRepository(int capacity) {
		this.capacity = capacity;
	}

	public static CategoryRepository getInstance() {
		if (instance == null) {
			instance = new CategoryRepository();
		}
		return instance;
	}

	public static CategoryRepository getInstance(int capacity) {
		if (instance == null) {
			instance = new CategoryRepository(capacity);
		}
		return instance;
	}
}
