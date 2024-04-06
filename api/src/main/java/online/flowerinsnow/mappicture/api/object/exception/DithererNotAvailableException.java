package online.flowerinsnow.mappicture.api.object.exception;

/**
 * <p>预留异常，当提供的颜色选择器无可用抖动算法时抛出</p>
 */
public class DithererNotAvailableException extends RuntimeException {
    public DithererNotAvailableException() {
        super();
    }

    public DithererNotAvailableException(String message) {
        super(message);
    }

    public DithererNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public DithererNotAvailableException(Throwable cause) {
        super(cause);
    }
}
