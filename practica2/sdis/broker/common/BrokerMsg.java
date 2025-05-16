package sdis.broker.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BrokerMsg extends Remote {
    String hello() throws RemoteException;
    String auth(String username, String password) throws RemoteException;
    public void add2Q(String mensaje) throws RemoteException;
    public void add2Q(String nombreCola, String mensaje) throws RemoteException;
    public String readQ() throws RemoteException;
    public String readQ(String nombreCola) throws RemoteException;
    public String peekQ() throws RemoteException;
    public String peekQ(String nombreCola) throws RemoteException;
    public String deleteQ(String nombreCola) throws RemoteException;
    public String getQueueList() throws RemoteException;
}
