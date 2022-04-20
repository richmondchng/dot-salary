package richmond.swe.dotsalary.config;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Exception handler.
 * @author richmondchng
 */
@Slf4j
@ControllerAdvice
public class RequestExceptionHandler {

    /**
     * Generic error handling for runtime exceptions.
     *
     * @param ex exception
     * @return ResponseEntity
     */
    @ExceptionHandler(value = {RuntimeException.class})
    public ResponseEntity<ErrorBody> handleRuntimeException(final RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorBody.builder()
                        .error(ex.getMessage())
                        .build());
    }

    @Builder
    @Getter
    static class ErrorBody {
        private String error;
    }
}
