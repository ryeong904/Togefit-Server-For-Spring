package Togefit.server.response;

public enum ErrorCode {
    USER_NOT_FOUND(400, "해당 유저를 찾지 못했습니다."),
    DUPLICATE_USER(400, "이 아이디는 현재 사용중입니다. 다른 아이디를 입력해주세요.");
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

    ErrorCode(int status, String message){
        this.status = status;
        this.message = message;
    }
}
