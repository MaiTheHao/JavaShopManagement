package main.repositories;

import main.lib.Query;
import main.models.Product;

public class ProductRepository extends Repository<Product>{

	public ProductRepository() {		
	}
	
	public ProductRepository(int capacity) {
		this.capacity = capacity;
	}
	
	Query<Product> sortByPrice(){
		return this.query().sort((e1, e2) -> Double.compare(e1.getPrice(), e2.getPrice()));
	}
	
	Query<Product> sortByPrice(SortOrder order){
		int flag = (order == SortOrder.ASC) ? 1 : -1;
		return this.query().sort((e1, e2) -> Double.compare(e1.getPrice(), e2.getPrice()) * flag);
	}
	
	Query<Product> sortByQuantity(){
		return this.query().sort((e1, e2) -> Integer.compare(e1.getQuantity(), e2.getQuantity()));
	}
	
	Query<Product> sortByQuantity(SortOrder order){
		int flag = (order == SortOrder.ASC) ? 1 : -1;
		return this.query().sort((e1, e2) -> Integer.compare(e1.getQuantity(), e2.getQuantity()) *flag);
	}
	
}

