package ite.gateway.gatewayserver.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
@Data
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
public class RoleExceptionHandler extends RuntimeException{
    private final HttpStatus status;
    private final String message;
}
