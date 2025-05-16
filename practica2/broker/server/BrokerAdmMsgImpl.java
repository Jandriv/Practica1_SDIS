package sdis.broker.server;

import sdis.broker.common.BrokerAdmMsg;
import sdis.broker.common.BrokerMsg;
import sdis.utils.MultiMap;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentHashMap;

public class BrokerAdmMsgImpl extends UnicastRemoteObject implements BrokerAdmMsg {
    private ConcurrentHashMap<String, String> registroUsuarios;
    private final MultiMap<String, String> mapa;
    protected BrokerAdmMsgImpl() throws RemoteException {
        super();
        this.mapa = new MultiMap<>();
        this.registroUsuarios = new ConcurrentHashMap<>();
        registroUsuarios.put("cllamas", "qwerty");
        registroUsuarios.put("hector", "lkjlkj");
        registroUsuarios.put("sdis", "987123");
        registroUsuarios.put("admin","$%&/()=");
    }

    @Override
    public String hello() throws RemoteException {
        return "";
    }

    @Override
    public String auth(String username, String password) throws RemoteException {
        return "";
    }

    @Override
    public void add2Q(String mensaje) throws RemoteException {

    }

    @Override
    public void add2Q(String nombreCola, String mensaje) throws RemoteException {

    }

    @Override
    public String readQ() throws RemoteException {
        return "";
    }

    @Override
    public String readQ(String nombrePlaylist) throws RemoteException {
        return "";
    }

    @Override
    public String peekQ() throws RemoteException {
        return "";
    }

    @Override
    public String peekQ(String nombreCola) throws RemoteException {
        return "";
    }

    @Override
    public String deleteQ(String nombreCola) throws RemoteException {
        return "";
    }

    @Override
    public String getQueueList() throws RemoteException {
        return "";
    }
}
