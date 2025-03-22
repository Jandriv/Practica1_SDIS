package sdis.broker.common;

public class Prueba {
    public static void main(String [] args) {
        try {
            MensajeProtocolo mp = new MensajeProtocolo(Primitiva.INFO, "hola") ;
            System.out.println(mp);
            mp = new MensajeProtocolo(Primitiva.READQ, "ca") ;
            System.out.println(mp);
            mp = new MensajeProtocolo(Primitiva.READQ, "cb") ;
            System.out.println(mp);
            mp = new MensajeProtocolo(Primitiva.MSG, "mens") ;
            System.out.println(mp);
            mp = new MensajeProtocolo(Primitiva.ADDMSG, "c1", "m1.1") ;
            System.out.println(mp);
            mp = new MensajeProtocolo(Primitiva.ADDED) ;
            System.out.println(mp);
            mp = new MensajeProtocolo(Primitiva.EMPTY) ;
            System.out.println(mp);
            mp = new MensajeProtocolo(Primitiva.READQ) ;
            System.out.println(mp);
        } catch (MalMensajeProtocoloException mmpe) {
            System.err.println(mmpe) ;
        }
    }
}
