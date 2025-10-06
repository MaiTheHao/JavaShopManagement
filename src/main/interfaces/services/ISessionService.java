package main.interfaces.services;

public interface ISessionService {
    void create(Long userId);

    void remove(Long userId);

    boolean isActive(Long userId);

    long getRemainingSeconds(Long userId);
}
