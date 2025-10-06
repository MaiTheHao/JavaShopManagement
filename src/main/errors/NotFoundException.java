package main.errors;

public class NotFoundException extends AppException {
    public NotFoundException(String message) {
        super(message);
    }

    public static NotFoundException from(Exception e) {
        return new NotFoundException(e.getMessage());
    }
}
