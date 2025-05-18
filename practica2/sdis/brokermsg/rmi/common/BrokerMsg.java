package sdis.brokermsg.rmi.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BrokerMsg extends Remote {
    String hello() throws RemoteException, NotAuthException;
    public void bye(String token) throws RemoteException, NotAuthException;
    String auth(String username, String password) throws RemoteException, NotAuthException;
    public void add2Q(String mensaje,String Token) throws RemoteException, NotAuthException;
    public void add2Q(String nombreCola, String mensaje,String Token) throws RemoteException, NotAuthException;
    public String readQ(String Token) throws RemoteException, NotAuthException;
    public String readQ(String nombreCola,String Token) throws RemoteException, NotAuthException;
    public String peekQ(String Token) throws RemoteException, NotAuthException;
    public String peekQ(String nombreCola,String Token) throws RemoteException, NotAuthException;
    public String deleteQ(String nombreCola,String Token) throws RemoteException, NotAuthException;
    public String getQueueList(String Token) throws RemoteException, NotAuthException;
}
