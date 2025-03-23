package sdis.broker.client.unit;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import sdis.broker.common.MalMensajeProtocoloException;
import sdis.broker.common.MensajeProtocolo;
import sdis.broker.common.Primitiva;

public class ReadQ {

    static ObjectOutputStream oos;
    static ObjectInputStream ois;

    public static void main(String args[]) throws IOException {
        java.net.Socket sock = new java.net.Socket("localhost", 3000);
        boolean fin = false;
        try {
            oos = new java.io.ObjectOutputStream(sock.getOutputStream());
            ois = new java.io.ObjectInputStream(sock.getInputStream());
            MensajeProtocolo minfo = (MensajeProtocolo) ois.readObject();
            if (minfo.getPrimitiva().equals(Primitiva.ERROR)) {
                System.out.println(minfo);
                fin = true;
            } else {
                System.out.println(minfo);
            }
            if (!fin) {
                pruebaPeticionRespuesta(new MensajeProtocolo(Primitiva.ADDMSG, "Bienvenida", "Hola buenas tardes"));
                MensajeProtocolo mi = (MensajeProtocolo) ois.readObject();
                System.out.println("< " + mi);
                pruebaPeticionRespuesta(new MensajeProtocolo(Primitiva.READQ, "Bienvenida"));
                MensajeProtocolo mr = (MensajeProtocolo) ois.readObject();
                System.out.println("< " + mr);
            }

        } catch (Exception e) {
            System.err.println("<Cliente: Excepcion: " + e);
            e.printStackTrace();

        } finally {
            oos.close();
            ois.close();
            sock.close();
        }
    }

    // Prueba una interacción de escritura y lectura con el servidor
    static void pruebaPeticionRespuesta(MensajeProtocolo mp)
            throws java.io.IOException, MalMensajeProtocoloException, ClassNotFoundException {
        System.out.println("> " + mp);
        oos.writeObject(mp);
    }
}