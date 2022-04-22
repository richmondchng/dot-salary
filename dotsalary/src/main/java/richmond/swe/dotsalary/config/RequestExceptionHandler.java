package richmond.swe.dotsalary.config;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import richmond.swe.dotsalary.controller.dto.UploadDTO;
import richmond.swe.dotsalary.exception.FileProcessException;

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
        log.error("Exception when processing request {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorBody.builder()
                        .error(ex.getMessage())
                        .build());
    }

    /**
     * Error handling for FileUploadException.
     *
     * @param ex FileUploadException
     * @return ResponseEntity
     */
    @ExceptionHandler(value = {FileProcessException.class})
    public ResponseEntity<UploadDTO> handleFileUploadException(final FileProcessException ex) {
        log.error("FileUploadException when processing request {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(UploadDTO.builder()
                        .success(0)
                        .build());
    }

    @Builder
    @Getter
    static class ErrorBody {
        private String error;
    }
}
