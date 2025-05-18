package sdis.brokermsg.rmi.server;

import java.net.Authenticator;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class LauncherRMI {
    public static int NThreads = 5;   //# hilos del ThreadPool
    public static int ClientesUsados = 0;
    public static int pushed = 0;
    public static int pulled = 0;

    public static void main(String args[]) {
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
    }
}