package Togefit.server.response.error;

public class Message {
    private String result;
    private String reason;

    public Message(){
        this.result = null;
        this.reason = null;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}