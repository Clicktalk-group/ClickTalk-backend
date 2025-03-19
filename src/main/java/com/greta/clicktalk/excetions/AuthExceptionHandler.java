package com.greta.clicktalk.excetions;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;

@RestControllerAdvice
public class AuthExceptionHandler {

@ExceptionHandler(SecurityException.class)
    public ResponseEntity<String> handleSecurityException(SecurityException e) {
    return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
}

@ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<String> handleExpiredJwtException(ExpiredJwtException e) {
    String message = """
                {
                "message": "Token expired",
                "type": "JWT Error"
                "status":"401 Unauthorized"
                }
                """;
    return new ResponseEntity<>(message, HttpStatus.UNAUTHORIZED);
}

@ExceptionHandler(JwtException.class)
public ResponseEntity<String> handleJwtException(JwtException e) {
    String message = """
                {
                "message": "Token is invalid or malformed.",
                "type": "JWT Error"
                "status":"401 Unauthorized"
                }
                """;
    return new ResponseEntity<>(message, HttpStatus.UNAUTHORIZED);
}

@ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentialsException(BadCredentialsException e) {
    return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
}

@ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalStateException(IllegalStateException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }
}
