package sdis.broker.client.unit;

import sdis.broker.common.BrokerMsg;

import java.rmi.Naming;

public class Add2L {
    public static void main(String[] args) {
        try {
            String host = "localhost";
            BrokerMsg or = (BrokerMsg) Naming.lookup("rmi://" + host + "/id1");
            System.out.println(or.hello());
            if(args.length == 1){
                or.add2Q(args[0]);
            }if(args.length == 2){
                or.add2Q(args[0],args[1]);
            }else{
                or.add2Q("Este es un mensaje enviado de manera automatica");
            }
            System.out.println("Mensaje añadido");
        }catch (java.rmi.RemoteException re) {
            System.err.println("<Cliente: Excepción RMI:" + re);
            re.printStackTrace();

        } catch (Exception e) {
            System.err.println("<Cliente: Excepcion: " + e);
            e.printStackTrace();
        }
    }

}
