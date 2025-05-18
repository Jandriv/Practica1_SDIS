package sdis.brokermsg.rmi.common;

public class NotAuthException extends RuntimeException {
    public NotAuthException(String message) {
        super(message);
    }
}
