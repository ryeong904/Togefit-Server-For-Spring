package Togefit.server.response.error;

public class ErrorCode {
    private int status;
    private String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ErrorCode(String message){
        this.status = 400;
        this.message = message;
    }

    public ErrorCode(int status, String message){
        this.status = status;
        this.message = message;
    }
}
