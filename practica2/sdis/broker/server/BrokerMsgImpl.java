package sdis.broker.server;

import sdis.broker.common.BrokerMsg;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentHashMap;

import sdis.broker.common.Strings;
import sdis.utils.MultiMap;

public class BrokerMsgImpl extends UnicastRemoteObject implements BrokerMsg{
    private ConcurrentHashMap<String, String> registroUsuarios;
    private final MultiMap<String, String> mapa;
    protected BrokerMsgImpl() throws RemoteException {
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
        return Strings.WELCOME_MESSAGE;
    }

    @Override
    public String auth(String username, String password) throws RemoteException {
        if (registroUsuarios.containsKey(username) && registroUsuarios.get(username).equals(password)) {//Si el usuario y contraseña están en el multimap
            return Strings.USER_LOGGED_SUCCESSFULLY;
        }else {
            return Strings.USER_LOGGED_UNSUCCESFULLY;
        }
    }

    @Override
    public void add2Q(String mensaje) throws RemoteException {
        mapa.push("DEFAULT", mensaje);
    }

    @Override
    public void add2Q(String nombreCola, String mensaje) throws RemoteException {
        mapa.push(nombreCola, nombreCola);
    }

    @Override
    public String readQ() throws RemoteException {
        return mapa.pop("DEFAULT");
    }

    @Override
    public String readQ(String nombreCola) throws RemoteException {
        return mapa.pop(nombreCola);
    }

    @Override
    public String peekQ() throws RemoteException {
        return mapa.get("DEFAULT");
    }

    @Override
    public String peekQ(String nombreCola) throws RemoteException {
        return mapa.get(nombreCola);
    }

    @Override
    public String deleteQ(String nombreCola) throws RemoteException {
        String mensaje;
        if (null == (mensaje = mapa.pop(nombreCola))) {
            return "EMPTY";
        } else {
            while(mensaje!= null){
                mensaje = mapa.pop(nombreCola);
            }
            return "DELETED";
        }
    }

    @Override
    public String getQueueList() throws RemoteException {

        return "hola" +mapa.hashCode();
    }
}
