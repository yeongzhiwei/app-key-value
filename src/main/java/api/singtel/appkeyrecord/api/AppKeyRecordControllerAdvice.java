package api.singtel.appkeyrecord.api;

import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.Data;

@RestControllerAdvice(basePackageClasses = AppKeyRecordController.class)
public class AppKeyRecordControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AppKeyRecordNotFoundException.class)
    ResponseEntity<ApiError> handleAppKeyRecordNotFound(AppKeyRecordNotFoundException ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ApiError apiError = ApiError.create(status, ex.getMessage());
        return ResponseEntity.status(status).body(apiError);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<ApiError> handleConstraintViolation(ConstraintViolationException ex) {
        String message = ex.getMessage().substring(ex.getMessage().indexOf(":") + 2);
        return handleNonResponseEntityException(message, HttpStatus.BAD_REQUEST);
    }

    private final ResponseEntity<ApiError> handleNonResponseEntityException(String message, HttpStatus status) {
        ApiError apiError = ApiError.create(status, message);
        return ResponseEntity.status(status).body(apiError);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        String message = "Could not parse JSON in the response body";
        return handleExceptionInternalMessage(ex, message, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getDefaultMessage()).collect(Collectors.joining("; "));
        return handleExceptionInternalMessage(ex, message, headers, status, request);
    }

    private final ResponseEntity<Object> handleExceptionInternalMessage(Exception ex, String message,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiError apierror = ApiError.create(status, message);
        return handleExceptionInternal(ex, apierror, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        if (body == null) {
            body = ApiError.create(status, "No message");
        }
        return new ResponseEntity<>(body, headers, status);
    }

    @Data
    static class ApiError {
        private final int status;
        private final String error;
        private final String message;

        private static ApiError create(HttpStatus status, String message) {
            return new ApiError(status.value(), status.name(), message);
        }
    }

}