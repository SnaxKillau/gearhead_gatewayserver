package ite.gateway.gatewayserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalException {
    @ExceptionHandler(RoleExceptionHandler.class)
    public ResponseEntity<String> roleExceptionHandler(RoleExceptionHandler e){
        return ResponseEntity.status(HttpStatus.OK)
                .body(e.getMessage());
    }

}
