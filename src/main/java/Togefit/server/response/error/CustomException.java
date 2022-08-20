package Togefit.server.response.error;

public class CustomException extends RuntimeException{
    private final Error error;

    public CustomException(Error error) {
        this.error = error;
    }

    public Error getErrorCode() {
        return error;
    }
}
