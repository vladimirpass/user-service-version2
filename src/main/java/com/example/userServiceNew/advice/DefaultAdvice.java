package com.example.userServiceNew.advice;

import com.example.userServiceNew.dto.Response;
import com.example.userServiceNew.exception.DeleteException;
import com.example.userServiceNew.exception.NoContentException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class DefaultAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler(DeleteException.class)
    public ResponseEntity<Response> notFoundException(Exception e){
        Response response = new Response(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(NoContentException.class)
    public ResponseEntity<Response> noContentException(NoContentException e){
        Response response = new Response(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }

}
