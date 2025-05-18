package sdis.brokermsg.rmi.server;

import sdis.brokermsg.rmi.common.BrokerMsg;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentHashMap;

import sdis.brokermsg.rmi.common.NotAuthException;
import sdis.utils.MultiMap;

public class BrokerMsgImpl extends UnicastRemoteObject implements BrokerMsg{
    private ConcurrentHashMap<String, String> registroUsuarios;
    private final MultiMap<String, String> mapa;
    private final AuthenticatorImpl authenticator;
    public BrokerMsgImpl(AuthenticatorImpl authenticator) throws RemoteException {
        super();
        this.authenticator = authenticator;
        this.mapa = new MultiMap<>();
        this.registroUsuarios = new ConcurrentHashMap<>();
        registroUsuarios.put("cllamas", "qwerty");
        registroUsuarios.put("hector", "lkjlkj");
        registroUsuarios.put("sdis", "987123");
        registroUsuarios.put("admin","$%&/()=");
    }

    @Override
    public String hello() throws RemoteException {
        return "HELLO";
    }
    @Override
    public void bye(String token) throws RemoteException, NotAuthException  {
        authenticator.checkToken(token);
        authenticator.disconnect(token);
    }

    @Override
    public String auth(String username, String password) throws RemoteException {
        String token = authenticator.connect(username,password);
        if (!token.equals("Credenciales incorrectas")) {//Si el usuario y contraseña están en el multimap
            return token;
        }else {
            return token;
        }
    }

    @Override
    public void add2Q(String mensaje, String Token) throws RemoteException, NotAuthException {
        authenticator.checkToken(Token);
        mapa.push("DEFAULT", mensaje);
    }

    @Override
    public void add2Q(String nombreCola, String mensaje,String Token) throws RemoteException, NotAuthException {
        authenticator.checkToken(Token);
        mapa.push(nombreCola, nombreCola);
    }

    @Override
    public String readQ(String Token) throws RemoteException {
        authenticator.checkToken(Token);
        return mapa.pop("DEFAULT");
    }

    @Override
    public String readQ(String nombreCola,String Token) throws RemoteException, NotAuthException {
        authenticator.checkToken(Token);
        return mapa.pop(nombreCola);
    }

    @Override
    public String peekQ(String Token) throws RemoteException, NotAuthException {
        authenticator.checkToken(Token);
        return mapa.peek("DEFAULT");
    }

    @Override
    public String peekQ(String nombreCola,String Token) throws RemoteException, NotAuthException {
        authenticator.checkToken(Token);
        return mapa.peek(nombreCola);
    }

    @Override
    public String deleteQ(String nombreCola,String Token) throws RemoteException, NotAuthException {
        authenticator.checkToken(Token);
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
    public String getQueueList(String Token) throws RemoteException, NotAuthException {
        authenticator.checkToken(Token);
        String mensaje = "";
        for(String Cola : mapa.keySet()){
            mensaje = mensaje.concat(Cola + " ");
        }
        return mensaje;
    }
}
