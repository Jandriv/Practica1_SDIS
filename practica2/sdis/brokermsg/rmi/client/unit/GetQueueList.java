package sdis.brokermsg.rmi.client.unit;

import sdis.brokermsg.rmi.common.BrokerMsg;

import java.rmi.Naming;

public class GetQueueList {
    public static void main(String[] args) {
        try {
            String host = "localhost";
            BrokerMsg or = (BrokerMsg) Naming.lookup("rmi://" + host + "/id1");
            System.out.println(or.hello());
            String token = or.auth("admin","$%&/()=");
            System.out.println(or.getQueueList(token));
            System.out.println("Listas");

        }catch (java.rmi.RemoteException re) {
            System.err.println("<Cliente: Excepción RMI:" + re);
            re.printStackTrace();

        } catch (Exception e) {
            System.err.println("<Cliente: Excepcion: " + e);
            e.printStackTrace();
        }
    }
}
