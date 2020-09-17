package com.org.kaweel.reactive.config.handle;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class CustomException extends RuntimeException {

    private HttpStatus statusCode;
    private ResponseMsg responseMsg;

    public CustomException(HttpStatus statusCode) {
        this.statusCode = statusCode;
        this.responseMsg = new ResponseMsg(statusCode.getReasonPhrase());
    }

    public CustomException(HttpStatus statusCode, String message) {
        this.statusCode = statusCode;
        this.responseMsg = new ResponseMsg(message);
    }

    @Data
    @NoArgsConstructor
    public static class ResponseMsg {

        @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Bangkok")
        private Date date = new Date();
        private String message;

        public ResponseMsg(HttpStatus httpStatus) {
            this.message = httpStatus.getReasonPhrase();
        }

        public ResponseMsg(String message) {
            this.message = message;
        }
    }
}
