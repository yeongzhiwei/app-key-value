package api.singtel.appkeyrecord;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.Data;

@RestControllerAdvice(basePackageClasses = AppKeyRecordController.class)
public class AppKeyRecordControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AppKeyRecordNotFoundException.class)
    ResponseEntity<ApiError> handleEmployeeNotFound(AppKeyRecordNotFoundException ex) {
        ApiError apiError = ApiError.create(HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
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