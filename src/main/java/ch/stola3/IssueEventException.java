package ch.stola3;

/**
 * Created by stola3 on 21.03.15.
 */
public class IssueEventException extends Exception {

    public IssueEventException() {
        super();
    }

    public IssueEventException(String message) {
        super(message);
    }

    public IssueEventException(String message, Throwable cause) {
        super(message, cause);
    }

    public IssueEventException(Throwable cause) {
        super(cause);
    }

    protected IssueEventException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
