package be.cytomine.appstore.exceptions.handlers;

import be.cytomine.appstore.dto.responses.errors.AppStoreError;
import be.cytomine.appstore.dto.responses.errors.ErrorBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class DefaultApiExceptionHandler {
    @ExceptionHandler({ Exception.class })
    public final ResponseEntity<AppStoreError> handleException(Exception e) {
        log.info("bad request 500 error");
        e.printStackTrace();
        AppStoreError error = ErrorBuilder.buildServerError(e);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
