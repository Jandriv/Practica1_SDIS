package sdis.brokermsg.rmi.client.unit;

import sdis.brokermsg.rmi.common.BrokerMsg;

import java.rmi.Naming;

public class ContinuousReadQ {
    public static void main(String[] args) {
        try {
            String host = "localhost";
            BrokerMsg or = (BrokerMsg) Naming.lookup("rmi://" + host + "/id1");
            System.out.println(or.hello());
            String token = or.auth("admin","$%&/()=");
            if(args.length == 1){
                String mensaje = or.readQ(args[0], token);
                while(mensaje != null){
                    System.out.println(mensaje);
                    mensaje = or.readQ(args[0],token);
                }
            }else{
                String mensaje = or.readQ(token);
                while(mensaje != null){
                    System.out.println(mensaje);
                    mensaje = or.readQ(token);
                }
            }



        }catch (java.rmi.RemoteException re) {
            System.err.println("<Cliente: Excepción RMI:" + re);
            re.printStackTrace();

        } catch (Exception e) {
            System.err.println("<Cliente: Excepcion: " + e);
            e.printStackTrace();
        }
    }
}
