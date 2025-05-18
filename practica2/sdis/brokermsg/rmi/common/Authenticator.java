package sdis.brokermsg.rmi.common;

public interface Authenticator {
    String connect(String username, String password);
    void disconnect(String tokenAuth);
}
