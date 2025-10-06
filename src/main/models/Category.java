package main.models;

import java.time.LocalDateTime;

public class Category extends Entity {
	private String description;

	public Category(Long id, String name, String description) {
		this.setId(id);
		this.setName(name);
		this.setDescription(description);
		this.setCreatedAt(LocalDateTime.now());
		this.setUpdatedAt(LocalDateTime.now());
	}

	public Category(Long id, String name) {
		this(id, name, "");
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}