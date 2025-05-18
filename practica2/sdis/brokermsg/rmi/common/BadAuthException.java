package sdis.brokermsg.rmi.common;

public class BadAuthException extends RuntimeException {
    public BadAuthException(String message) {
        super(message);
    }
}
