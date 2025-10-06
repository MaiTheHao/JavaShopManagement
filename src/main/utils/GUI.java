package main.utils;

import java.util.Scanner;
import main.services.AuthService;
import main.services.ProductService;
import main.services.SessionService;
import main.models.User;
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
        if (instance == null)
            instance = new GUI();
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

    public void printMenu() {
        System.out.println("---------- Shop Management Menu ----------");
        if (!isLoggedIn()) {
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("0. Exit");
        } else {
            System.out.println("Welcome, " + currentUser.getName() + "!");
            System.out.println("Your role: " + (currentUser.getRole() != null ? currentUser.getRole().name() : "USER"));
            long remaining = SessionService.getInstance().getRemainingSeconds(currentUser.getId());
            System.out.println("Session expires in: " + remaining + " seconds");
            System.out.println("1. View Products");
            System.out.println("2. Add Product (Admin required)");
            System.out.println("3. Delete Product (Admin required)");
            System.out.println("4. Search Product By Name");
            System.out.println("5. Logout");
            System.out.println("0. Exit");
        }
        System.out.println("------------------------------------------");
    }

    private void printProductTable(Product[] products, int page, String title) {
        if (products == null || products.length == 0) {
            System.out.println("No products found.");
            return;
        }
        System.out.println(title + " (Page " + page + "):");
        System.out.println("+---------------------------+----------------------+------------+----------+");
        System.out.println(String.format("| %-25s | %-20s | %-10s | %-8s |", "ID", "Name", "Price", "Quantity"));
        System.out.println("+---------------------------+----------------------+------------+----------+");
        for (Product p : products) {
            System.out.println(String.format("| %-25s | %-20s | %-10.2f | %-8d |",
                    p.getId(), p.getName(), p.getPrice(), p.getQuantity()));
        }
        System.out.println("+---------------------------+----------------------+------------+----------+");
    }

    private void waitForEnter(Scanner scanner) {
        System.out.println(">> enter <<");
        scanner.nextLine();
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public void showProductList(Scanner scanner) {
        int page = 1, size = 5;
        System.out.print("Enter page number (default 1): ");
        String pageInput = scanner.nextLine();
        if (!pageInput.isEmpty()) {
            try {
                page = Math.max(1, Integer.parseInt(pageInput));
            } catch (NumberFormatException e) {
                System.out.println("Invalid page number. Using default page 1.");
            }
        }
        Product[] products = ProductService.getInstance().getList(page, size);
        printProductTable(products, page, "Product List");
        waitForEnter(scanner);
    }

    public void addProduct(Scanner scanner) {
        if (currentUser == null || !AuthService.getInstance().isAdmin(currentUser.getId())) {
            System.out.println("You do not have permission to add products.");
            waitForEnter(scanner);
            return;
        }
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
            ProductService.getInstance().add(product);
            System.out.println("Product added successfully!");
        } catch (Exception e) {
            System.out.println("Failed to add product: " + e.getMessage());
        }
        waitForEnter(scanner);
    }

    public void deleteProduct(Scanner scanner) {
        if (currentUser == null || !AuthService.getInstance().isAdmin(currentUser.getId())) {
            System.out.println("You do not have permission to delete products.");
            waitForEnter(scanner);
            return;
        }
        System.out.println("=== Delete Product ===");
        try {
            System.out.print("Enter Product ID to delete: ");
            long id = Long.parseLong(scanner.nextLine());
            ProductService.getInstance().delete(id);
            System.out.println("Product deleted successfully!");
        } catch (Exception e) {
            System.out.println("Failed to delete product: " + e.getMessage());
        }
        waitForEnter(scanner);
    }

    public void searchProductByName(Scanner scanner) {
        System.out.println("=== Search Product By Name ===");
        System.out.print("Enter keyword: ");
        String keyword = scanner.nextLine();
        int page = 1, size = 5;
        System.out.print("Enter page number (default 1): ");
        String pageInput = scanner.nextLine();
        if (!pageInput.isEmpty()) {
            try {
                page = Math.max(1, Integer.parseInt(pageInput));
            } catch (NumberFormatException e) {
                System.out.println("Invalid page number. Using default page 1.");
            }
        }
        Product[] products = ProductService.getInstance().search(
                keyword,
                main.enumerations.ProductSearchableFields.NAME,
                page,
                size);
        printProductTable(products, page, "Search Results");
        waitForEnter(scanner);
    }
}