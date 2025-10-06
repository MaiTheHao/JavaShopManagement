package main.interfaces.services;

import main.models.User;

public interface IAuthService {
	public User login(String email, String password);

	public void register(String email, String password, String checkPassword, String name);

	public void logout(Long uid);

	public boolean validSession(Long uid);

	public boolean isAdmin(Long uid);
}
