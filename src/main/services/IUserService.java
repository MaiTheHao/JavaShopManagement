package main.services;

import main.models.User;

public interface IUserService {
	public User getById(int id);
	public User update(User data);
	public void delete(int id);
}
