package com.javangarda.fantacalcio.user.infrastructure.port.adapter.http.api;

import com.javangarda.fantacalcio.commons.http.BadRequestResponseDTO;
import com.javangarda.fantacalcio.commons.http.ResponseDTO;
import org.springframework.boot.context.config.ResourceNotFoundException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class MethodArgumentNotValidExceptionHandler {

    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseDTO methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return new BadRequestResponseDTO(ex.getBindingResult());
    }

    @ResponseStatus(NOT_FOUND)
    @ResponseBody
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseDTO handleNotFoundException(ResourceNotFoundException ex) {
        return new ResponseDTO(NOT_FOUND.value(), "fantacalcio.user.notfound");
    }

}