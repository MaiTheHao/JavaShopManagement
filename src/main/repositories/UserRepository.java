package main.repositories;

import main.models.User;

public class UserRepository extends Repository<User> {
	private static UserRepository instance;

	private UserRepository() {
	}

	private UserRepository(int capacity) {
		this.capacity = capacity;
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
		return this.query().find(e -> e.getEmail().equalsIgnoreCase(email));
	}
}
