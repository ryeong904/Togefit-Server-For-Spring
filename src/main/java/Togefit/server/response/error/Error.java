package Togefit.server.response.error;

public class Error {
    private int status;
    private String message;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Error(String message){
        this.status = 400;
        this.message = message;
    }

    public Error(int status, String message){
        this.status = status;
        this.message = message;
    }
}
