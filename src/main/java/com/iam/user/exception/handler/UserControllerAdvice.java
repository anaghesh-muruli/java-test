package com.iam.user.exception.handler;

import com.iam.user.common.ApiResponseHandler;
import com.iam.user.exception.response.ErrorResponse;
import com.iam.user.exception.custom.UserAlreadyRegisteredException;
import com.iam.user.exception.custom.UserManagementException;
import com.iam.user.exception.custom.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.*;

@ControllerAdvice
public class UserControllerAdvice {

    Logger LOGGER = LoggerFactory.getLogger(UserControllerAdvice.class);

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {

      Throwable rootCause = getRootCause(ex);
      LOGGER.debug("General Exception Handler. Root cause: "+rootCause.toString());
      return createExceptionResponse(new Date(), ex.toString(), request.getDescription(false), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(BindException ex) {

        Throwable rootCause = getRootCause(ex);
        LOGGER.debug("BindException Handler. Root cause: "+rootCause.toString());
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) ->{

            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });
        return new ResponseEntity<>(ApiResponseHandler.generateFailureApiResponse("Invalid input",HttpStatus.BAD_REQUEST.value(), errors),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyRegisteredException.class)
    public ResponseEntity<Object> handleUserRegisteredException(final UserAlreadyRegisteredException ex, WebRequest request) {
        Throwable rootCause = getRootCause(ex);
        LOGGER.debug("UserAlreadyRegisteredException Handler. Root cause: "+rootCause.toString());
        return createExceptionResponse(new Date(), ex.getErrorMessage(), request.getDescription(false), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(final UserNotFoundException ex, WebRequest request) {
        Throwable rootCause = getRootCause(ex);
        LOGGER.debug("UserAlreadyRegisteredException Handler. Root cause: "+rootCause.toString());
        return createExceptionResponse(new Date(), ex.getErrorMessage(), request.getDescription(false), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserManagementException.class)
    public ResponseEntity<Object> handleUserRegisteredException(final UserManagementException ex, WebRequest request) {
        Throwable rootCause = getRootCause(ex);
        LOGGER.debug("UserAlreadyRegisteredException Handler. Root cause: "+rootCause.toString());
        return createExceptionResponse(new Date(), ex.getErrorMessage(), request.getDescription(false), HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<Object> createExceptionResponse(Date date, String errorMessage,
                                                           String details, HttpStatus statusCode) {
        ErrorResponse exceptionResponse = ErrorResponse.builder()
                .timestamp(date)
                .error(errorMessage)
                .details(details)
                .build();
        return new ResponseEntity<>(exceptionResponse, statusCode);
    }

    private Throwable getRootCause(Throwable ex) {
        if(ex.getCause() == null) {
            return ex;
        }
        return ex.getCause();
    }
}
