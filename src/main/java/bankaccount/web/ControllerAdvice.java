package bankaccount.web;

import bankaccount.service.ServiceException;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

@RestControllerAdvice
public class ControllerAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<?> handleServiceException(WebRequest request, ServiceException ex) {
        final DefaultErrorAttributes errorAttributes = new DefaultErrorAttributes();
        final Map<String, Object> attributes = errorAttributes.getErrorAttributes(request, false);
        attributes.put("errorCode", ex.getCode());
        final HttpStatus status = HttpStatus.BAD_REQUEST;
        attributes.put("status", status.value());
        attributes.put("error", status.getReasonPhrase());
        return new ResponseEntity<>(attributes, status);
    }
}
