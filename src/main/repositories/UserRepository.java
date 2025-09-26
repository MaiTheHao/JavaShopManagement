package main.repositories;
import main.models.User;

public class UserRepository extends Repository<User> {
	public User findByEmail(String email) {
		return this.query().find(e -> e.getEmail().equalsIgnoreCase(email));
	}
	
	public UserRepository() {
	}
	
	public UserRepository(int capacity) {
		this.capacity = capacity;
	}
}
