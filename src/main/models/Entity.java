package main.models;

import java.time.LocalDateTime;

public abstract class Entity {
	protected int id;
	protected String name;
	protected LocalDateTime createdAt;
	protected LocalDateTime updatedAt;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if(name == null || name.trim().isEmpty()) throw new IllegalArgumentException("Name must be not null");
		this.name = name;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		if(createdAt == null) throw new IllegalArgumentException("Created at must not be null");
		if (createdAt.isAfter(LocalDateTime.now())) throw new IllegalArgumentException("Created at must be not in the future");
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdateAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		if(updatedAt == null) throw new IllegalArgumentException("Updated at must not be null");
		if (updatedAt.isAfter(LocalDateTime.now())) throw new IllegalArgumentException("Updated at must be not  in the future");
		this.updatedAt = updatedAt;
	}
}
