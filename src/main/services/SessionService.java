package main.services;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import main.interfaces.services.ISessionService;

public class SessionService implements ISessionService {
    private static SessionService instance;
    private static final Long SESSION_TIMEOUT_SECS = 1 * 60L;
    private static final Map<Long, LocalDateTime> sessions = new HashMap<>();

    private SessionService() {
    }

    public static SessionService getInstance() {
        if (instance == null) {
            instance = new SessionService();
        }
        return instance;
    }

    @Override
    public void create(Long userId) {
        sessions.put(userId, LocalDateTime.now());
    }

    @Override
    public void remove(Long userId) {
        sessions.remove(userId);
    }

    @Override
    public boolean isActive(Long userId) {
        LocalDateTime start = sessions.get(userId);
        if (start == null)
            return false;
        long exp = Duration.between(start, LocalDateTime.now()).toSeconds();
        boolean active = exp < SESSION_TIMEOUT_SECS;
        if (!active)
            sessions.remove(userId);
        return active;
    }

    @Override
    public long getRemainingSeconds(Long userId) {
        LocalDateTime start = sessions.get(userId);
        if (start == null)
            return 0;
        long exp = Duration.between(start, LocalDateTime.now()).toSeconds();
        long remaining = SESSION_TIMEOUT_SECS - exp;
        if (remaining <= 0) {
            sessions.remove(userId);
            return 0;
        }
        return remaining;
    }
}
