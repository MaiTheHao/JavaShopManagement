package main.services;

import main.models.Product;

public interface IProductService {
	public Product getById(int id);
	public Product updata(Product data);
	public void delete(int id);
	public Product[] getList(int page, int limit, ProductSortableFields sortBy);
	public Product[] search(ProductSearchableFields searchBy, int page, int limit, ProductSortableFields sortBy);
}
