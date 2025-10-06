package main.repositories;

import main.abstracts.repositories.Repository;
import main.models.Category;

public class CategoryRepository extends Repository<Category> {
	private static CategoryRepository instance;

	private CategoryRepository() {
		this(DEFAULT_CAPACITY);
	}

	private CategoryRepository(int capacity) {
		this.capacity = capacity;
		this.datas = new Category[capacity];
		this.size = 0;
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