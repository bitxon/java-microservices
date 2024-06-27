package bitxon.common.exception;

public class DirtyTrickException extends RuntimeException {

    public DirtyTrickException(String message) {
        super("Dirty Trick: " + message);
    }
}
