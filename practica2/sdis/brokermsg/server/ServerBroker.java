package sdis.brokermsg.server;

import sdis.brokermsg.rmi.server.AuthenticatorImpl;
import sdis.brokermsg.rmi.server.BrokerMsgImpl;
import sdis.utils.MultiMap;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerBroker {
    public static int NThreads = 5;   //# hilos del ThreadPool
    public static int ClientesUsados = 0;
    public static int pushed = 0;
    public static int pulled = 0;
    public static void main(String[] args) {
        try {
            AuthenticatorImpl auth= new AuthenticatorImpl();
            BrokerMsgImpl oRemoto = new BrokerMsgImpl(auth);
            /**
             * TO CRETAE & LAUNCH THE RMI-REGISTRY VIA SOFT
             */
            Registry registro =
                    LocateRegistry.createRegistry(1099);
            Registry registroAdm =
                    LocateRegistry.createRegistry(1100);
            /**
             * TO GET THE RMI-REGISTRY REF OF IP-ADDRESS
             */
            //Registry registro = LocateRegistry.getRegistry("localhost");
            registro.rebind("id1", oRemoto);

            System.err.println("Servidor preparado");
        } catch (Exception e) {
            System.err.println("Excepción del servidor: " + e.toString());
            e.printStackTrace();
        }
        int PUERTO = 3000;  //puerto de servicio
        MultiMap<String, String>
                mapa = new MultiMap();
        java.util.concurrent.ExecutorService exec =
                java.util.concurrent.Executors.newFixedThreadPool(NThreads);
        try {
            java.net.ServerSocket sock = new java.net.ServerSocket(PUERTO);
            System.err.println("Servidor: WHILE [INICIANDO]");
            Thread mainServer = new Thread(() -> {
                try {
                    while (true) {
                        java.net.Socket socket = sock.accept();
                        try {
                            if(ClientesUsados>=NThreads*2){
                                System.err.println("Conexion Rechazada maximo numero de clientes");
                            }else{
                                sdis.brokermsg.tcp.server.Sirviente serv =
                                        new sdis.brokermsg.tcp.server.Sirviente(socket, mapa);
                                exec.execute(serv);
                                ClientesUsados++;
                            }

                        } catch (java.io.IOException ioe) {
                            System.out.println("Servidor: WHILE [ERR ObjectStreams]");
                        }
                    }  //fin-while
                } catch (java.io.IOException ioe) {
                    System.err.println("Servidor: WHILE [Error.E/S]");
                } catch (Exception e) {
                    System.err.println("Servidor: WHILE [Error.execute]");
                }
            }, "RUN(WHILE)");  //fin-newThread separado para servidor
            mainServer.start();
            System.out.println("Servidor: [CORRIENDO]");
        } catch (java.io.IOException ioe) {
            System.out.println("Servidor: [ERR SOCKET]");
        }

    }
}
