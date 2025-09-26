package main.repositories;

import main.models.Category;

public class CategoryRepository extends Repository<Category>{
	public CategoryRepository() {
	}
	public CategoryRepository(int capacity) {
		this.capacity = capacity;
	}
}
