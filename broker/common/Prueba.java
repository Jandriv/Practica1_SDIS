package sdis.broker.common;

public class Prueba {
    public static void main(String [] args) {
        try {
            MensajeProtocolo mp = new MensajeProtocolo(Primitiva.INFO, "hola") ;
            System.out.println(mp);
            mp = new MensajeProtocolo(Primitiva.READL, "ca") ;
            System.out.println(mp);
            mp = new MensajeProtocolo(Primitiva.READL, "cb") ;
            System.out.println(mp);
            mp = new MensajeProtocolo(Primitiva.MEDIA, "mens") ;
            System.out.println(mp);
            mp = new MensajeProtocolo(Primitiva.ADD2L, "c1", "m1.1") ;
            System.out.println(mp);
            mp = new MensajeProtocolo(Primitiva.ADDED) ;
            System.out.println(mp);
            mp = new MensajeProtocolo(Primitiva.EMPTY) ;
            System.out.println(mp);
            mp = new MensajeProtocolo(Primitiva.READL) ;
            System.out.println(mp);
        } catch (MalMensajeProtocoloException mmpe) {
            System.err.println(mmpe) ;
        }
    }
}
