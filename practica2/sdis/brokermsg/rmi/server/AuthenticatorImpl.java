package sdis.brokermsg.rmi.server;

import sdis.brokermsg.rmi.common.Authenticator;
import sdis.brokermsg.rmi.common.NotAuthException;
import sdis.utils.Digest;


import java.util.concurrent.ConcurrentHashMap;

public class AuthenticatorImpl implements Authenticator{
    private ConcurrentHashMap<String, String> registroUsuarios;
    private ConcurrentHashMap<String, String> tokens;
    public AuthenticatorImpl() {
        this.tokens = new ConcurrentHashMap<>();
        this.registroUsuarios = new ConcurrentHashMap<>();
        registroUsuarios.put("cllamas", "qwerty");
        registroUsuarios.put("hector", "lkjlkj");
        registroUsuarios.put("sdis", "987123");
        registroUsuarios.put("admin","$%&/()=");
    }
    @Override
    public String connect(String username, String password){
        if (registroUsuarios.containsKey(username) && registroUsuarios.get(username).equals(password)) {//Si el usuario y contraseña están en el multimap
            Digest digest = new Digest();
            String token = digest.generateHash();
            tokens.put(token,username);
            return token;
        }else {
            return "Credenciales incorrectas";
        }
    }
    @Override
    public void disconnect(String tokenAuth){
            tokens.remove(tokenAuth);
    }
    void checkToken(String token) throws NotAuthException{
        if (!tokens.containsKey(token)) {
            throw new NotAuthException("Token not found");
        }
    }
}
