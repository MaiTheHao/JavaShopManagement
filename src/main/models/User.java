package main.models;

import java.time.LocalDateTime;

import main.enumerations.Role;
import main.errors.BadRequestException;

public class User extends Entity {
	protected String email;
	protected String password;
	protected Role role;

	public User(Long id, String email, String password, String name) {
		this(id, email, password, name, Role.USER);
	}

	public User(Long id, String email, String password, String name, Role role) {
		this.setId(id);
		this.setEmail(email);
		this.setPassword(password);
		this.setName(name);
		this.setRole(role);
		this.setCreatedAt(LocalDateTime.now());
		this.setUpdatedAt(LocalDateTime.now());
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		if (email == null || email.isEmpty()) {
			throw new BadRequestException("Email must not be empty");
		}

		if (!email.contains("@") || email.startsWith("@") || email.endsWith("@")) {
			throw new BadRequestException("Email must contain a valid '@' separator");
		}

		String[] parts = email.split("@");
		if (parts.length != 2) {
			throw new BadRequestException("Email must contain exactly one '@' symbol");
		}

		String localPart = parts[0];
		String domainPart = parts[1];

		String localRegex = "^[A-Za-z0-9._%+-]+$";
		if (!localPart.matches(localRegex)) {
			throw new BadRequestException("Email local part contains invalid characters");
		}

		String domainRegex = "^[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
		if (!domainPart.matches(domainRegex)) {
			throw new BadRequestException("Email domain format is invalid");
		}

		if (email.length() > 254) {
			throw new BadRequestException("Email is too long (max 254 characters)");
		}

		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		if (password == null || password.isEmpty())
			throw new BadRequestException("Password must be not empty");

		int length = password.length();
		if (length < 8) {
			throw new BadRequestException("Password must be at least 8 characters long");
		}
		if (length > 64) {
			throw new BadRequestException("Password must not exceed 64 characters");
		}

		this.password = password;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		if (role == null)
			throw new BadRequestException("Role must not be null");
		this.role = role;
	}
}
