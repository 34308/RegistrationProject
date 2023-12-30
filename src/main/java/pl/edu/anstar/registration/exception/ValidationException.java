package pl.edu.anstar.registration.exception;


public class ValidationException extends RuntimeException {
    private final String errorMessage;

    public ValidationException(String errorCode, String errorMessage) {
        super(errorCode);
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}

