package main.models;

public class User extends Entity {
	protected String email;
	protected String password;
	protected Role role;

	public User(String email, String password, String name) {
		this.setEmail(email);
		this.setPassword(password);
		this.setName(name);
	}

	public User(String email, String password, Role role, String name) {
		this.setEmail(email);
		this.setPassword(password);
		this.setRole(role);
		this.setName(name);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		if (email == null || email.isEmpty()) throw new IllegalArgumentException("Email must be not empty");
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		if (password == null || password.isEmpty()) throw new IllegalArgumentException("Password must be not empty");
		this.password = password;
	}

	public Role getRole() {
		return this.role;
	}

	public void setRole(Role role) {
		if (role == null) throw new IllegalArgumentException("Role must be not empty");
		this.role = role;
	}

}
