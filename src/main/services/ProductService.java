package main.services;

import main.interfaces.services.IProductService;
import main.models.Product;
import main.repositories.ProductRepository;
import main.utils.Query;
import main.enumerations.ProductSearchableFields;
import main.enumerations.ProductSortableFields;
import main.enumerations.SortOrder;
import main.errors.AppException;
import main.errors.BadRequestException;
import main.errors.NotFoundException;

public class ProductService implements IProductService {
    private static ProductService instance;
    private final ProductRepository productRepo;

    private ProductService() {
        productRepo = ProductRepository.getInstance();
    }

    public static ProductService getInstance() {
        if (instance == null) {
            instance = new ProductService();
        }
        return instance;
    }

    @Override
    public Product getById(Long id) {
        return productRepo.findById(id);
    }

    @Override
    public void add(Product data) {
        try {
            productRepo.add(data);
        } catch (BadRequestException e) {
            throw new BadRequestException("Invalid product data: " + e.getMessage());
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException("Failed to add product: " + e.getMessage());
        }
    }

    @Override
    public void update(Product data) {
        try {
            productRepo.update(data);
        } catch (BadRequestException e) {
            throw new BadRequestException("Invalid product data: " + e.getMessage());
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException("Failed to update product: " + e.getMessage());
        }
    }

    @Override
    public void delete(Long id) {
        try {
            productRepo.remove(id);
        } catch (NotFoundException e) {
            throw new NotFoundException("Product with ID " + id + " not found: " + e.getMessage());
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException("Failed to delete product: " + e.getMessage());
        }
    }

    @Override
    public Product[] getList(int page, int limit) {
        return productRepo.query().paginate(page, limit).getResult();
    }

    @Override
    public Product[] getList(int page, int limit, ProductSortableFields sortBy, SortOrder order) {
        Query<Product> q = productRepo.query();

        q = sorted(q, sortBy, order);

        return q.paginate(page, limit).getResult();
    }

    @Override
    public Product[] search(String keyword, ProductSearchableFields searchBy, int page, int limit) {
        return search(keyword, searchBy, page, limit, ProductSortableFields.NAME, SortOrder.ASC);
    }

    @Override
    public Product[] search(String keyword, ProductSearchableFields searchBy, int page, int limit,
            ProductSortableFields sortBy,
            SortOrder order) {

        Query<Product> q = productRepo.query();

        q = switch (searchBy) {
            case NAME -> q.filter(p -> p.getName().toLowerCase().contains(keyword.toLowerCase()));
            default -> q;
        };

        q = sorted(q, sortBy, order);

        return q.paginate(page, limit).getResult();
    }

    private Query<Product> sorted(Query<Product> query, ProductSortableFields sortBy, SortOrder order) {
        return switch (sortBy) {
            case PRICE -> productRepo.sortByPrice(query, order);
            case QUANTITY -> productRepo.sortByQuantity(query, order);
            case NAME -> productRepo.sortByName(query, order);
            case CREATED_AT -> productRepo.sortByCreatedAt(query, order);
        };
    }

    @Override
    public void deleteAll() {
        productRepo.clear();
    }
}
