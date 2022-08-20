package Togefit.server.response.error;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.nio.charset.Charset;


public class ErrorResponse {
    public static ResponseEntity toResponseEntity(Error error){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        Message message = new Message();
        message.setResult("error");
        message.setReason(error.getMessage());

        return new ResponseEntity<>(message, headers, HttpStatus.valueOf(error.getStatus()));
    }
}
