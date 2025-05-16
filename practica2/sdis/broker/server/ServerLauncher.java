package sdis.broker.server;
import sdis.utils.MultiMap;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerLauncher {
    public static int NThreads = 5;   //# hilos del ThreadPool
    public static int ClientesUsados = 0;
    public static int pushed = 0;
    public static int pulled = 0;

    public static void main(String args[]) {
        try {
            BrokerMsgImpl oRemoto = new BrokerMsgImpl();
            BrokerAdmMsgImpl oRemotoAdm = new BrokerAdmMsgImpl();
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
            registroAdm.rebind("id2", oRemotoAdm);

            System.err.println("Servidor preparado");
        } catch (Exception e) {
            System.err.println("Excepción del servidor: " + e.toString());
            e.printStackTrace();
        }
    }
}