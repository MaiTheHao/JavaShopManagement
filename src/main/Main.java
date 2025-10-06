package main;

import main.utils.GUI;
import main.enumerations.Role;
import main.models.Product;
import main.models.User;
import main.repositories.ProductRepository;
import main.repositories.UserRepository;

import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class Main {
	private static final GUI gui = GUI.getInstance();

	static {
		ProductRepository productRepo = ProductRepository.getInstance();
		if (productRepo.query().count() == 0) {
			String[] names = {
					"Wireless Mouse", "Mechanical Keyboard", "Gaming Headset", "USB-C Hub", "Smartphone Stand",
					"Men's T-Shirt", "Women's Hoodie", "Sneakers", "Leather Belt", "Baseball Cap",
					"Coffee Maker", "Electric Kettle", "Air Fryer", "Vacuum Cleaner", "LED Desk Lamp",
					"Fiction Novel", "Science Textbook", "Comic Book", "Notebook Journal", "Children's Storybook"
			};
			String[] descriptions = {
					"Ergonomic wireless mouse with silent click.",
					"RGB backlit mechanical keyboard for fast typing.",
					"Surround sound gaming headset with noise cancel mic.",
					"7-in-1 USB-C hub with HDMI and card reader.",
					"Adjustable aluminum stand for smartphones and tablets.",
					"100% cotton classic fit t-shirt for men.",
					"Soft fleece hoodie with minimalist design.",
					"Comfortable sneakers with breathable fabric.",
					"Genuine leather belt with silver buckle.",
					"Adjustable cotton cap with embroidered logo.",
					"Automatic coffee maker with timer function.",
					"Quick-boil stainless steel electric kettle.",
					"Compact air fryer with 3.5L capacity.",
					"Cordless vacuum cleaner with strong suction.",
					"Dimmable LED desk lamp with touch control.",
					"Bestselling fiction novel with rich storytelling.",
					"Comprehensive textbook covering modern physics.",
					"Popular Japanese comic translated to English.",
					"Hardcover notebook for journaling and sketches.",
					"Illustrated storybook for early readers."
			};
			for (int i = 0; i < 20; i++) {
				productRepo.add(new Product(
						(long) (i + 1),
						names[i],
						15.0 + (i * 3.5),
						10 + (i % 5) * 2,
						1,
						descriptions[i]));
			}
		}

		UserRepository userRepo = UserRepository.getInstance();
		if (!userRepo.existsByEmail("admin@gmail.com")) {
			userRepo.add(new User(
					LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
					"admin@gmail.com",
					"12345678",
					"Admin",
					Role.ADMIN));
		}
		if (!userRepo.existsByEmail("user@gmail.com")) {
			userRepo.add(new User(
					LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) + 1,
					"user@gmail.com",
					"12345678",
					"User"));
		}
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		boolean running = true;

		while (running) {
			try {
				gui.printMenu();
				System.out.print("Select option: ");
				String input = scanner.nextLine();

				if (!gui.isLoggedIn()) {
					switch (input) {
						case "1":
							System.out.print("Email: ");
							String email = scanner.nextLine();
							System.out.print("Password: ");
							String password = scanner.nextLine();
							if (gui.login(email, password)) {
								System.out.println("Login successful!");
							}
							System.out.println(">> enter <<");
							scanner.nextLine();
							System.out.print("\033[H\033[2J");
							System.out.flush();
							break;
						case "2":
							System.out.print("Email: ");
							String regEmail = scanner.nextLine();
							System.out.print("Password: ");
							String regPassword = scanner.nextLine();
							System.out.print("Confirm Password: ");
							String regCheckPassword = scanner.nextLine();
							System.out.print("Name: ");
							String regName = scanner.nextLine();
							main.services.AuthService.getInstance().register(regEmail, regPassword, regCheckPassword,
									regName);
							System.out.println("Register successful! Please login.");
							System.out.println(">> enter <<");
							scanner.nextLine();
							System.out.print("\033[H\033[2J");
							System.out.flush();
							break;
						case "0":
							running = false;
							break;
						default:
							System.out.println("Invalid option.");
							System.out.println(">> enter <<");
							scanner.nextLine();
							System.out.print("\033[H\033[2J");
							System.out.flush();
					}
				} else {
					switch (input) {
						case "1":
							gui.showProductList(scanner);
							break;
						case "2":
							gui.addProduct(scanner);
							break;
						case "3":
							gui.deleteProduct(scanner);
							break;
						case "4":
							gui.searchProductByName(scanner);
							break;
						case "5":
							gui.logout();
							System.out.println("Logged out.");
							System.out.println(">> enter <<");
							scanner.nextLine();
							System.out.print("\033[H\033[2J");
							System.out.flush();
							break;
						case "0":
							running = false;
							break;
						default:
							System.out.println("Invalid option.");
							System.out.println(">> enter <<");
							scanner.nextLine();
							System.out.print("\033[H\033[2J");
							System.out.flush();
					}
				}
			} catch (Exception e) {
				System.out.println("Error: " + e.getMessage());
				System.out.println(">> enter <<");
				scanner.nextLine();
				System.out.print("\033[H\033[2J");
				System.out.flush();
			}
		}
		System.out.println("Application closed.");
		scanner.close();
	}
}