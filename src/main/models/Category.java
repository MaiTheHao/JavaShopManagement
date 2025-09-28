package main.models;

public class Category extends Entity {
	private String description;

	public Category(String name, String description) {
		this.setName(name);
		this.setDescription(description);
	}

	public Category(String name) {
		this(name, "");
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}