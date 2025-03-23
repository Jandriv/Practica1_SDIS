package sdis.broker.server;

import sdis.broker.common.*;
import sdis.utils.*;

import java.util.concurrent.ConcurrentHashMap;

public class Sirviente implements Runnable {
    private static ConcurrentHashMap<String, String> registroUsuarios = new ConcurrentHashMap<>();
    private static BlacklistManager managerConexiones = new BlacklistManager(3);
    private static BlacklistManager managerLogins = new BlacklistManager(2);
    private final java.net.Socket socket;
    private final MultiMap<String, String> mapa;
    private final java.io.ObjectOutputStream oos;
    private final java.io.ObjectInputStream ois;
    private final int ns;
    private static java.util.concurrent.atomic.AtomicInteger nInstance = new java.util.concurrent.atomic.AtomicInteger();

    public Sirviente(java.net.Socket s, MultiMap<String, String> c) throws java.io.IOException {
        this.socket = s;
        this.mapa = c;
        this.ns = nInstance.getAndIncrement();
        this.oos = new java.io.ObjectOutputStream(socket.getOutputStream());
        this.ois = new java.io.ObjectInputStream(socket.getInputStream());
    } // se invoca en el Servidor, usualmente

    public void run() {
        String clientIP = socket.getInetAddress().getHostAddress();
        String userlogged = "NA";
        try {
            registroUsuarios.put("cllamas", "qwerty");
            registroUsuarios.put("hector", "lkjlkj");
            registroUsuarios.put("sdis", "987123");
            registroUsuarios.put("admin", "$%&/()=");
            System.out.println(Strings.SERVER_WAITING);
            boolean fin = false;
            boolean authored = false;
            MensajeProtocolo ms;

            if (managerConexiones.isIPBaneada(clientIP)) {
                ms = new MensajeProtocolo(Primitiva.ERROR, Strings.MAX_CONNECTIONS_REACHED_ERROR);
                oos.writeObject(ms);
                fin = true;
            } else if (managerLogins.isIPBaneada(clientIP)) {
                managerConexiones.registraIntento(clientIP);
                ms = new MensajeProtocolo(Primitiva.ERROR, Strings.MAX_LOGIN_ATTEMPTS_REACHED_ERROR);
                oos.writeObject(ms);
                fin = true;
            } else {
                managerConexiones.registraIntento(clientIP);
                System.out.println(
                        "[BM] (connections) for " + clientIP + " = " + managerConexiones.getIntentos(clientIP));
                ms = new MensajeProtocolo(Primitiva.INFO, Strings.WELCOME_MESSAGE);
                oos.writeObject(ms);
            }

            while (!fin) {
                String mensaje; // String multipurpose
                MensajeProtocolo me = (MensajeProtocolo) ois.readObject();
                // me y ms: mensajes entrante y saliente
                System.out.println("Sirviente: " + ns + " from: " + clientIP + ": [ME: " + me); // depuracion me
                switch (me.getPrimitiva()) {
                    case XAUTH:
                        String username = me.getIdCola();
                        String password = me.getMensaje();
                        // Si el usuario y la contraseña estan en el multimap
                        if (registroUsuarios.containsKey(username) && registroUsuarios.get(username).equals(password)) {

                            authored = true;
                            userlogged = username;
                            oos.writeObject(ms);
                        } else {
                            managerLogins.registraIntento(clientIP);
                            if (managerLogins.isIPBaneada(clientIP)) {
                                ms = new MensajeProtocolo(Primitiva.ERROR, Strings.MAX_LOGIN_ATTEMPTS_REACHED_ERROR);
                                oos.writeObject(ms);
                                fin = true;
                            } else {
                                ms = new MensajeProtocolo(Primitiva.NOTAUTH, Strings.LOGIN_ERROR);
                                System.out.println("[BM] (login fails) for " + clientIP + " = "
                                        + managerLogins.getIntentos(clientIP));
                                oos.writeObject(ms);
                            }
                        }
                        break;
                    case READQ:
                        mensaje = mapa.pop(me.getIdCola());
                        if (null != mensaje) {
                            ms = new MensajeProtocolo(Primitiva.MSG, mensaje);
                            System.out.println("Sirviente: " + ns + ": [ME: " + ms); // depuracion me
                            Servidor.pulled++;
                            oos.writeObject(ms);
                        } else {
                            ms = new MensajeProtocolo(Primitiva.EMPTY);
                            System.out.println("Sirviente: " + ns + ": [ME: " + ms); // depuracion me
                            oos.writeObject(ms);
                        }
                        break;

                    case STATE:
                        if (authored && userlogged.equals("admin")) {
                            ms = new MensajeProtocolo(Primitiva.STATE,
                                    "Hilos maximos: " + Servidor.NThreads + " Hilos ocupados: "
                                            + Servidor.ClientesUsados + " Mensajes enviados: " + Servidor.pushed
                                            + " Mensajes recibidos: " + Servidor.pulled);
                            oos.writeObject(ms);
                        } else {
                            ms = new MensajeProtocolo(Primitiva.NOTAUTH, Strings.NO_ADMIN);
                            oos.writeObject(ms);
                        }
                        break;
                    case DELETEQ:
                        if (authored && userlogged.equals("admin")) {
                            String idCola = me.getIdCola(); // Obtener el ID de la cola especificada
                            if (mapa.containsKey(idCola)) { // Verificar si la cola existe

                                while ((mensaje = mapa.pop(idCola)) != null) {

                                }
                                ms = new MensajeProtocolo(Primitiva.DELETED);
                            } else {
                                ms = new MensajeProtocolo(Primitiva.EMPTY); // La cola no existe o está vacía
                            }
                            System.out.println("Sirviente: " + ns + ": [ME: " + ms); // Depuración
                            oos.writeObject(ms);
                        } else {
                            ms = new MensajeProtocolo(Primitiva.NOTAUTH, Strings.NO_ADMIN); // No autorizado
                            oos.writeObject(ms);
                        }
                        break;
                    case ADDMSG:
                        if (authored) {
                            synchronized (mapa) {
                                if (!mapa.containsKey(me.getIdCola())) {
                                    if (userlogged.equals("admin")) {
                                        mapa.push(me.getIdCola(), me.getMensaje());
                                        ms = new MensajeProtocolo(Primitiva.ADDED);
                                    } else {
                                        ms = new MensajeProtocolo(Primitiva.NOTAUTH, Strings.NO_ADMIN);
                                    }
                                } else {
                                    mapa.push(me.getIdCola(), me.getMensaje());
                                    ms = new MensajeProtocolo(Primitiva.ADDED);
                                }
                                mapa.notify(); // Despierta un sirviente esperando en un bloqueo de "mapa"
                            }
                            System.out.println("Sirviente: " + ns + ": [ME: " + ms); // depuración
                            if (ms.getPrimitiva() == Primitiva.ADDED) {
                                Servidor.pushed++;
                            }
                            oos.writeObject(ms);
                        } else {
                            ms = new MensajeProtocolo(Primitiva.NOTAUTH, Strings.LOGIN_REQUIRED);
                            oos.writeObject(ms);
                        }
                        break;
                    default:
                        ms = new MensajeProtocolo(Primitiva.BADCODE, Strings.UNEXPECTTED_ERROR);
                        oos.writeObject(ms);

                } // fin del selector segun el mensaje entrante

            }
        } catch (Exception io) {

        }
        managerConexiones.clientedesconectado(clientIP);
        System.out.println("Cliente desconectado. Usuario asociado = " + userlogged);
        System.out.println("[BM] (connections) for " + clientIP + " = " + managerConexiones.getIntentos(clientIP));
        Servidor.ClientesUsados--;

    }
}
