package main.repositories;

import main.lib.Query;
import main.models.Product;

import main.enumerations.SortOrder;

public class ProductRepository extends Repository<Product> {
	private static ProductRepository instance;

	private ProductRepository() {
		this(DEFAULT_CAPACITY);
	}

	private ProductRepository(int capacity) {
		this.capacity = capacity;
		this.datas = new Product[capacity];
		this.size = 0;
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

	public Query<Product> sortByPrice() {
		return this.query().sort((e1, e2) -> Double.compare(e1.getPrice(), e2.getPrice()));
	}

	public Query<Product> sortByPrice(SortOrder order) {
		int flag = (order == SortOrder.ASC) ? 1 : -1;
		return this.query().sort((e1, e2) -> Double.compare(e1.getPrice(), e2.getPrice()) * flag);
	}

	public Query<Product> sortByQuantity() {
		return this.query().sort((e1, e2) -> Integer.compare(e1.getQuantity(), e2.getQuantity()));
	}

	public Query<Product> sortByQuantity(SortOrder order) {
		int flag = (order == SortOrder.ASC) ? 1 : -1;
		return this.query().sort((e1, e2) -> Integer.compare(e1.getQuantity(), e2.getQuantity()) * flag);
	}
}