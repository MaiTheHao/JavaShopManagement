package main.utils;

import java.util.Scanner;
import main.services.AuthService;
import main.services.ProductService;
import main.services.SessionService;
import main.models.User;
import main.enumerations.Role;
import main.models.Product;

public class GUI {
    private static GUI instance;
    private User currentUser;

    static {
        System.out.println("Application is opened.");
    }

    private GUI() {

    }

    public static GUI getInstance() {
        if (instance == null) {
            instance = new GUI();
        }
        return instance;
    }

    public boolean login(String email, String password) {
        try {
            currentUser = AuthService.getInstance().login(email, password);
            return true;
        } catch (Exception e) {
            System.out.println("Login failed: " + e.getMessage());
            return false;
        }
    }

    public void logout() {
        if (currentUser != null) {
            AuthService.getInstance().logout(currentUser.getId());
            currentUser = null;
        }
    }

    public boolean isLoggedIn() {
        return currentUser != null && SessionService.getInstance().isActive(currentUser.getId());
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean hasRole(Role role) {
        if (currentUser == null)
            return false;
        return AuthService.getInstance().hasRole(currentUser.getId(), new Role[] { role });
    }

    public ProductService getProductService() {
        return ProductService.getInstance();
    }

    public AuthService getAuthService() {
        return AuthService.getInstance();
    }

    public SessionService getSessionService() {
        return SessionService.getInstance();
    }

    public void printMenu() {
        System.out.println("===== Shop Management Menu =====");
        if (!isLoggedIn()) {
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("0. Exit");
        } else {
            System.out.println("Welcome, " + currentUser.getName() + "!");
            System.out.println("1. View Products");
            System.out.println("2. Add Product");
            System.out.println("3. Logout");
            System.out.println("0. Exit");
        }
        System.out.println("================================");
    }

    public void showProductList(Scanner scanner) {
        int page = 1;
        int size = 5;
        System.out.print("Enter page number (default 1): ");
        String pageInput = scanner.nextLine();
        if (!pageInput.isEmpty()) {
            try {
                page = Integer.parseInt(pageInput);
                if (page < 1)
                    page = 1;
            } catch (NumberFormatException e) {
                System.out.println("Invalid page number. Using default page 1.");
                page = 1;
            }
        }

        Product[] products = getProductService().getList(page, size);
        if (products.length == 0) {
            System.out.println("No products found.");
        } else {
            System.out.println("Product List (Page " + page + "):");
            System.out.println(String.format("| %-25s | %-20s | %-10s | %-8s |",
                    "ID", "Name", "Price", "Quantity"));
            for (Product p : products) {
                System.out.println(p.toString());
            }
            System.out.println("+------+----------------------+------------+----------+");
        }
    }

    public void addProduct(Scanner scanner) {
        System.out.println("=== Add New Product ===");
        try {
            System.out.print("Name: ");
            String name = scanner.nextLine();
            System.out.print("Price: ");
            double price = Double.parseDouble(scanner.nextLine());
            System.out.print("Quantity: ");
            int quantity = Integer.parseInt(scanner.nextLine());
            System.out.print("Category ID: ");
            int categoryId = Integer.parseInt(scanner.nextLine());
            System.out.print("Description: ");
            String description = scanner.nextLine();

            long id = System.currentTimeMillis();

            Product product = new Product(id, name, price, quantity, categoryId, description);
            getProductService().add(product);
            System.out.println("Product added successfully!");
        } catch (Exception e) {
            System.out.println("Failed to add product: " + e.getMessage());
        }
    }
}
