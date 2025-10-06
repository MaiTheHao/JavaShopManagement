package main.repositories;

import main.abstracts.repositories.Repository;
import main.errors.BadRequestException;
import main.models.User;

public class UserRepository extends Repository<User> {
	private static UserRepository instance;

	private UserRepository() {
		this(DEFAULT_CAPACITY);
	}

	private UserRepository(int capacity) {
		this.capacity = capacity;
		this.datas = new User[capacity];
		this.size = 0;
	}

	public static UserRepository getInstance() {
		if (instance == null) {
			instance = new UserRepository();
		}
		return instance;
	}

	public static UserRepository getInstance(int capacity) {
		if (instance == null) {
			instance = new UserRepository(capacity);
		}
		return instance;
	}

	public User findByEmail(String email) {
		if (email == null || email.isEmpty())
			throw new BadRequestException("Email cannot be null or empty.");

		return this.query().find(e -> e.getEmail().trim().equalsIgnoreCase(email));
	}

	public boolean existsByEmail(String email) {
		if (email == null || email.isEmpty())
			throw new BadRequestException("Email cannot be null or empty.");

		return this.query().find(e -> e.getEmail().trim().equalsIgnoreCase(email)) != null;
	}
}