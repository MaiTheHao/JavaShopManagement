package main.models;

public class Product extends Entity {
	private double price;
	private int quantity;
	private int categoryId;
	private String description;

	public Product(String name, double price, int quantity) {
		this.setName(name);
		this.setPrice(price);
		this.setQuantity(quantity);
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		if (price < 0) throw new IllegalArgumentException("Price must be greater than or equal to 0");
		
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		if (quantity < 0) throw new IllegalArgumentException("Quantity must be greater than or equal to 0");
		this.quantity = quantity;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void inceaseQuantity(int amount) {
		if (amount > 0) quantity += amount;
	}

	public void decreaseQuantity(int amount) {
		 if (amount <= 0) return;
		 this.quantity = Math.max(0, this.quantity - amount);
	}

	public boolean isOutOfStock() {
		return quantity <= 0;
	}
}
