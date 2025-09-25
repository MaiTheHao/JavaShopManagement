package main.models;

public class Product extends Entity {
	private double price;
	private int quantity;
	private int categoryId;
	private String description;

	public Product(String name, double price, int quantity) {
		this.name = name;
		this.price = price;
		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		if (price < 0) {
			throw new IllegalArgumentException("Price is must > 0");
		}
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		if (quantity < 0) {
			throw new IllegalArgumentException("Quantity must be > 0");
		}
		this.quantity = quantity;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		if (categoryId < 0) {
			throw new IllegalArgumentException("CategotyId must be > 0");
		}
		this.categoryId = categoryId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void inceaseQuantity(int amount) {
		if (amount > 0) {
			quantity += amount;
		}
	}

	public void decreaseQuantity(int amount) {
		if (amount > 0) {
			quantity -= amount;
		} else {
			if (quantity < 0) {
				quantity = 0;
			}
		}
	}

	public boolean isOutOfStock() {
		return quantity == 0;
	}
}
