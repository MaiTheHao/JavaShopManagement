package main;

import main.utils.GUI;
import main.errors.AppException;
import main.models.Product;

import java.util.Scanner;

public class Main {
	private static final GUI gui = GUI.getInstance();

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		boolean running = true;

		if (gui.getProductService().getList(1, 1).length == 0) {
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
				gui.getProductService().add(
						new Product(
								(long) (i + 1),
								names[i],
								15.0 + (i * 3.5),
								10 + (i % 5) * 2,
								1,
								descriptions[i]));
			}
		}

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
							gui.getAuthService().register(regEmail, regPassword, regCheckPassword, regName);
							System.out.println("Register successful! Please login.");

							break;
						case "0":
							running = false;
							break;
						default:
							System.out.println("Invalid option.");

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
							gui.logout();
							System.out.println("Logged out.");

							break;
						case "0":
							running = false;
							break;
						default:
							System.out.println("Invalid option.");

					}
				}
			} catch (AppException e) {
				System.out.println(e);

			} catch (Exception e) {
				System.out.println("Unexpected error: " + e.getMessage());

			}
		}
		System.out.println("Application closed.");
		scanner.close();
	}
}