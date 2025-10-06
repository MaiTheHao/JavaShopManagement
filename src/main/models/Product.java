package main.models;

import java.time.LocalDateTime;

import main.errors.BadRequestException;

public class Product extends Entity {
	private double price;
	private Integer quantity;
	private Integer categoryId;
	private String description;

	public Product(Long id, String name, double price, Integer quantity) {
		this(id, name, price, quantity, null, "");
	}

	public Product(Long id, String name, double price, Integer quantity, Integer categoryId, String description) {
		this.setId(id);
		this.setName(name);
		this.setPrice(price);
		this.setQuantity(quantity);
		this.setCategoryId(categoryId);
		this.setDescription(description);
		this.setCreatedAt(LocalDateTime.now());
		this.setUpdatedAt(LocalDateTime.now());
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		if (price < 0)
			throw new BadRequestException("Price must be greater than or equal to 0");

		this.price = price;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		if (quantity == null || quantity < 0)
			throw new BadRequestException("Quantity must be greater than or equal to 0");
		this.quantity = quantity;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void inceaseQuantity(Integer amount) {
		if (amount > 0)
			quantity += amount;
	}

	public void decreaseQuantity(Integer amount) {
		if (amount <= 0)
			return;
		this.quantity = Math.max(0, this.quantity - amount);
	}

	public boolean isOutOfStock() {
		return quantity <= 0;
	}

	@Override
	public String toString() {
		return String.format("| %-25d | %-20s | %-10.2f | %-8d |",
				getId(), getName(), getPrice(), getQuantity());
	}
}
