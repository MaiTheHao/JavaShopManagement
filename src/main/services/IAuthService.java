package main.services;

import main.models.User;

public interface IAuthService {
	public User login(String email, String password); 
	public boolean register(String email, String password, String checkPassword, String name);
	public boolean hasRole(int[] role);
	public boolean hasAllRoles(int[] role);
}
