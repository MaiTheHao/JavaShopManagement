package main.interfaces.services;

import main.enumerations.ProductSearchableFields;
import main.enumerations.ProductSortableFields;
import main.enumerations.SortOrder;
import main.models.Product;

public interface IProductService {
	Product getById(Long id);

	void add(Product data);

	void update(Product data);

	void delete(Long id);

	Product[] getList(int page, int limit);

	Product[] getList(int page, int limit, ProductSortableFields sortBy, SortOrder order);

	Product[] search(String keyword, ProductSearchableFields searchBy, int page, int limit);

	Product[] search(String keyword, ProductSearchableFields searchBy, int page, int limit,
			ProductSortableFields sortBy,
			SortOrder order);

	void deleteAll();
}