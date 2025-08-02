package seminario.invoicing.controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import seminario.invoicing.exceptions.InsufficientStockException;
import seminario.invoicing.exceptions.RepeatedDataRequestException;
import seminario.invoicing.exceptions.ResourceNotFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String TIMESTAMP = "timestamp";
    private static final String STATUS = "status";
    private static final String ERROR = "error";
    private static final String MESSAGE = "message";


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return handleExceptions("ResourceNotFound", ex.getMessage(),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        String message = String.format("Parameter '%s' must be type numeric (Long).", ex.getName());
        return handleExceptions("InvalidParameterType",message,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        Map<String, Object> body = new LinkedHashMap<>();
        body.put(TIMESTAMP, LocalDateTime.now());
        body.put(STATUS, HttpStatus.BAD_REQUEST.value());
        body.put("errors", errors);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<Object> handleInsufficientStockException() {
        String message = "Insufficient Stock for product";
        return handleExceptions("InsufficientStockException",message,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RepeatedDataRequestException.class)
    public ResponseEntity<Object> handleRepeatedDataRequestException(RepeatedDataRequestException ex) {
        String message = "This request can not be processed since is repeated in the dataBase ... " +  ex.getMessage();
        return handleExceptions("RepeatedDataRequestException",message,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        String message = "This request can not be processed since arguments do not match de expectations  ... " +  ex.getMessage();
        return handleExceptions("IllegalArgumentException",message,HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Object> handleExceptions(String errorName,String message,HttpStatus status){
        Map<String, Object> body = new HashMap<>();
        body.put(TIMESTAMP, LocalDateTime.now());
        body.put(STATUS,status);
        body.put(ERROR, errorName);
        body.put(MESSAGE, message);

        return new ResponseEntity<>(body, status);
    }
}