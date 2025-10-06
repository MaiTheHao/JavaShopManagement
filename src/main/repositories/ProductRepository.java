package main.repositories;

import main.models.Product;
import main.utils.Query;
import main.abstracts.repositories.Repository;
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
		return sortByPrice(SortOrder.ASC);
	}

	public Query<Product> sortByPrice(SortOrder order) {
		int flag = (order == SortOrder.ASC) ? 1 : -1;
		return this.query().sort((e1, e2) -> Double.compare(e1.getPrice(), e2.getPrice()) * flag);
	}

	public Query<Product> sortByPrice(Query<Product> base) {
		return sortByPrice(base, SortOrder.ASC);
	}

	public Query<Product> sortByPrice(Query<Product> base, SortOrder order) {
		int flag = (order == SortOrder.ASC) ? 1 : -1;
		return base.sort((e1, e2) -> Double.compare(e1.getPrice(), e2.getPrice()) * flag);
	}

	public Query<Product> sortByQuantity() {
		return sortByQuantity(SortOrder.ASC);
	}

	public Query<Product> sortByQuantity(SortOrder order) {
		int flag = (order == SortOrder.ASC) ? 1 : -1;
		return this.query().sort((e1, e2) -> Integer.compare(e1.getQuantity(), e2.getQuantity()) * flag);
	}

	public Query<Product> sortByQuantity(Query<Product> base) {
		return sortByQuantity(base, SortOrder.ASC);
	}

	public Query<Product> sortByQuantity(Query<Product> base, SortOrder order) {
		int flag = (order == SortOrder.ASC) ? 1 : -1;
		return base.sort((e1, e2) -> Integer.compare(e1.getQuantity(), e2.getQuantity()) * flag);
	}
}