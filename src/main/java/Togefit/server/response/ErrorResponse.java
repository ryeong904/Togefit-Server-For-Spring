package Togefit.server.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


public class ErrorResponse {
    private final int status;
    private final String message;

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public static ResponseEntity toResponseEntity(ErrorCode errorCode){
        return new ResponseEntity<>(errorCode.getMessage(), HttpStatus.valueOf(errorCode.getStatus()));
//        return ResponseEntity
//                .status(errorCode.getStatus())
//                .body(new ErrorResponse(errorCode.getStatus(), errorCode.getMessage()));
    }
}
