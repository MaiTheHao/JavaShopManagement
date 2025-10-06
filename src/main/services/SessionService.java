package main.services;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import main.interfaces.services.ISessionService;

public class SessionService implements ISessionService {
    private static SessionService instance;
    private static final Long SESSION_TIMEOUT_SECS = 1 * 60L; // 1 minute
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
        LocalDateTime startTime = sessions.get(userId);
        if (startTime == null)
            return false;
        long elapsedMillis = Duration.between(startTime, LocalDateTime.now()).toSeconds();
        boolean active = elapsedMillis < SESSION_TIMEOUT_SECS;
        if (!active)
            sessions.remove(userId);
        return active;
    }
}
