package main.errors;

public class AppException extends RuntimeException {
    public AppException(String message) {
        super(message);
    }

    public static AppException from(Exception e) {
        return new AppException(e.getMessage());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ": " + getMessage();
    }

}