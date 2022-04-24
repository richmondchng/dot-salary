package richmond.swe.dotsalary.exception;

/**
 * File upload exception.
 * @author richmondchng
 */
public class FileProcessException extends RuntimeException {

    public FileProcessException(final String message, final Throwable e) {
        super(message, e);
    }
}
