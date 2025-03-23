package sdis.broker.client.unit;



import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.Naming;

import sdis.broker.common.MalMensajeProtocoloException;
import sdis.broker.common.MensajeProtocolo;
import sdis.broker.common.Primitiva;

public class AuthAddMsg {
    //Ejemplo
	static ObjectOutputStream oos;
	static ObjectInputStream ois;
    public static void main(String args[]) throws IOException {
        java.net.Socket sock = new java.net.Socket("localhost", 3000);
        
		try {
        	oos = new java.io.ObjectOutputStream(sock.getOutputStream());
            ois = new java.io.ObjectInputStream(sock.getInputStream());
            MensajeProtocolo minfo = (MensajeProtocolo) ois.readObject();
            if (minfo.getPrimitiva().equals(Primitiva.ERROR)) {
                System.out.println(minfo);
                boolean fin = true;
            }else {
                System.out.println(minfo);
            }
            pruebaPeticionRespuesta(new MensajeProtocolo(Primitiva.XAUTH, "admin", "$%&/()="));
           	MensajeProtocolo mr = (MensajeProtocolo) ois.readObject();
           	System.out.println("< " + mr);
           	pruebaPeticionRespuesta(new MensajeProtocolo(Primitiva.ADDMSG, "Bienvenida", "Hola buenas tardes"));
           	mr = (MensajeProtocolo) ois.readObject();
           	System.out.println("< " + mr);
        } catch (java.rmi.RemoteException re) {
            System.err.println("<Cliente: Excepción RMI:" + re);
            re.printStackTrace();

        } catch (Exception e) {
            System.err.println("<Cliente: Excepcion: " + e);
            e.printStackTrace();

        }finally {
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
