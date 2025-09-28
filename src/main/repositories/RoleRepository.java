package main.repositories;

import main.models.Role;

public class RoleRepository extends Repository<Role> {
	private static RoleRepository instance;

	private RoleRepository() {
	}

	private RoleRepository(int capacity) {
		this.capacity = capacity;
	}

	public static RoleRepository getInstance() {
		if (instance == null) {
			instance = new RoleRepository();
		}
		return instance;
	}

	public static RoleRepository getInstance(int capacity) {
		if (instance == null) {
			instance = new RoleRepository(capacity);
		}
		return instance;
	}

}
