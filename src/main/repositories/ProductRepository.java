package main.repositories;

import main.lib.Query;
import main.models.Product;
import main.enumerations.SortOrder;

public class ProductRepository extends Repository<Product> {
	private static ProductRepository instance;

	private ProductRepository() {
	}

	private ProductRepository(int capacity) {
		this.capacity = capacity;
	}

	public static ProductRepository getInstance() {
		if (instance == null) {
			instance = new ProductRepository();
		}
		return instance;
	}

	public static ProductRepository getInstance(int capacity) {
		if (instance == null) {
			instance = new ProductRepository(capacity);
		}
		return instance;
	}

	Query<Product> sortByPrice() {
		return this.query().sort((e1, e2) -> Double.compare(e1.getPrice(), e2.getPrice()));
	}

	Query<Product> sortByPrice(SortOrder order) {
		int flag = (order == SortOrder.ASC) ? 1 : -1;
		return this.query().sort((e1, e2) -> Double.compare(e1.getPrice(), e2.getPrice()) * flag);
	}

	Query<Product> sortByQuantity() {
		return this.query().sort((e1, e2) -> Integer.compare(e1.getQuantity(), e2.getQuantity()));
	}

	Query<Product> sortByQuantity(SortOrder order) {
		int flag = (order == SortOrder.ASC) ? 1 : -1;
		return this.query().sort((e1, e2) -> Integer.compare(e1.getQuantity(), e2.getQuantity()) * flag);
	}

}
