package main.errors;

public class BadRequestException extends AppException {
    public BadRequestException(String message) {
        super(message);
    }

    public static BadRequestException from(Exception e) {
        return new BadRequestException(e.getMessage());
    }
}
