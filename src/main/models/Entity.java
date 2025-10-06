package main.models;

import java.time.LocalDateTime;

import main.errors.BadRequestException;

public abstract class Entity {
	protected Long id;
	protected String name;
	protected LocalDateTime createdAt;
	protected LocalDateTime updatedAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		if (id == null)
			throw new BadRequestException("Id must be greater than 0");
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (name == null || name.trim().isEmpty())
			throw new BadRequestException("Name must be not null");
		this.name = name;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		if (createdAt == null)
			throw new BadRequestException("Created at must not be null");
		if (createdAt.isAfter(LocalDateTime.now()))
			throw new BadRequestException("Created at must be not in the future");
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdateAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		if (updatedAt == null)
			throw new BadRequestException("Updated at must not be null");
		if (updatedAt.isAfter(LocalDateTime.now()))
			throw new BadRequestException("Updated at must be not  in the future");
		this.updatedAt = updatedAt;
	}
}
