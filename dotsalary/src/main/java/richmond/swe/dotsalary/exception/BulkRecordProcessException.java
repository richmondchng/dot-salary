package richmond.swe.dotsalary.exception;

/**
 * Bulk process record exception.
 * @author richmondchng
 */
public class BulkRecordProcessException extends RuntimeException {

    public BulkRecordProcessException(final String message, final Throwable e) {
        super(message, e);
    }
}
