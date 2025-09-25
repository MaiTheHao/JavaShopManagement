package models;

public class User extends Entity{
	protected String email;
	protected String password;
	protected int roled;
	public User(String email, String password, String name) {
		this.email = email;
		this.password = password;
		this.name = name;
	}
	public User(String email, String password, int roled, String name) {
		this.email = email;
		this.password = password;
		this.roled = roled;
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		if(email == null || email.isEmpty()) {
			throw new IllegalArgumentException("Email must be not empty");
		}
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		if(password == null || password.isEmpty()) {
			throw new IllegalArgumentException("Password must be not empty");
		}
		this.password = password;
	}
	public int getRoled() {
		return roled;
	}
	public void setRoled(int roled) {
		if(roled < 0) {
			throw new IllegalArgumentException("Role must be > 0");
		}
		this.roled = roled;
	}
	
}
