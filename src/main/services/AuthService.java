package main.services;

import main.interfaces.services.IAuthService;
import main.interfaces.services.ISessionService;
import main.enumerations.Role;
import main.models.User;
import main.repositories.UserRepository;
import main.errors.AppException;
import main.errors.BadRequestException;
import main.errors.NotFoundException;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class AuthService implements IAuthService {
    private static AuthService instance;
    private UserRepository userRepository;
    private ISessionService sessionService;

    private AuthService() {
        this.userRepository = UserRepository.getInstance();
        this.sessionService = SessionService.getInstance();
    }

    public static AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }
        return instance;
    }

    @Override
    public User login(String email, String password) {
        if (email == null || email.trim().isEmpty()) {
            throw new BadRequestException("Email cannot be null or empty.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new BadRequestException("Password cannot be null or empty.");
        }

        try {
            User user = userRepository.findByEmail(email);

            if (user == null || !user.getPassword().equals(password)) {
                throw new BadRequestException("Invalid email or password.");
            }

            sessionService.create(user.getId());
            return user;
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException("Login failed: " + e.getMessage());
        }
    }

    @Override
    public void register(String email, String password, String checkPassword, String name) {
        if (email == null || email.trim().isEmpty()) {
            throw new BadRequestException("Email cannot be null or empty.");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new BadRequestException("Password cannot be null or empty.");
        }

        if (name == null || name.trim().isEmpty()) {
            throw new BadRequestException("Name cannot be null or empty.");
        }

        if (!password.equals(checkPassword)) {
            throw new BadRequestException("Passwords do not match.");
        }

        try {
            boolean exists = userRepository.existsByEmail(email);
            if (exists) {
                throw new BadRequestException("Email already exists.");
            }
        } catch (Exception e) {
        }

        try {
            User newUser = new User(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC), email,
                    password, name);

            userRepository.add(newUser);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void logout(Long uid) {
        if (uid == null) {
            throw new BadRequestException("User ID cannot be null.");
        }

        try {
            sessionService.remove(uid);
        } catch (Exception e) {
            throw new AppException("Logout failed: " + e.getMessage());
        }
    }

    @Override
    public boolean validSession(Long uid) {
        if (uid == null) {
            throw new BadRequestException("User ID cannot be null.");
        }
        try {
            return sessionService.isActive(uid);
        } catch (Exception e) {
            throw new AppException("Validate session failed: " + e.getMessage());
        }
    }

    @Override
    public boolean hasRole(Long uid, Role[] roles) {
        if (uid == null) {
            throw new BadRequestException("User ID cannot be null.");
        }
        if (roles == null || roles.length == 0) {
            throw new BadRequestException("Roles cannot be null or empty.");
        }
        try {
            User user = userRepository.findById(uid);
            if (user == null) {
                throw new NotFoundException("User not found.");
            }
            for (Role role : roles) {
                if (user.getRole() == role) {
                    return true;
                }
            }
            return false;
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException("Check role failed: " + e.getMessage());
        }
    }

    @Override
    public boolean hasAllRoles(Long uid, Role[] roles) {
        if (uid == null) {
            throw new BadRequestException("User ID cannot be null.");
        }
        if (roles == null || roles.length == 0) {
            throw new BadRequestException("Roles cannot be null or empty.");
        }
        try {
            User user = userRepository.findById(uid);
            if (user == null) {
                throw new NotFoundException("User not found.");
            }
            for (Role role : roles) {
                if (user.getRole() != role) {
                    return false;
                }
            }
            return true;
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException("Check roles failed: " + e.getMessage());
        }
    }
}
