package main.errors;

public class InternalAppException extends AppException {
    public InternalAppException(String message) {
        super(message);
    }

    public static InternalAppException from(Exception e) {
        return new InternalAppException(e.getMessage());
    }
}
