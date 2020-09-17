package com.org.kaweel.reactive.config.handle;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import reactor.core.publisher.Mono;

@Slf4j
@ControllerAdvice
public class HandleException {

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity> exception(Exception e) {
        log.error("Exception msg : [{}] ", e.getMessage());
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomException.ResponseMsg()));
    }

    @ExceptionHandler(HttpClientErrorException.BadRequest.class)
    public Mono<ResponseEntity> badRequest(HttpClientErrorException.BadRequest e) {
        log.error("BadRequest Exception msg : {} [{}] ", e.getStatusCode(), e.getMessage());
        return Mono.just(ResponseEntity.status(e.getStatusCode()).body(new CustomException.ResponseMsg()));
    }

    @ExceptionHandler(CustomException.class)
    public Mono<ResponseEntity> custom(CustomException e) {
        log.error("CustomException msg : {} [{}] ", e.getStatusCode(), e.getMessage());
        return Mono.just(ResponseEntity.status(e.getStatusCode()).body(e.getResponseMsg()));
    }
}
