package sdis.broker.client.unit;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import sdis.broker.common.MalMensajeProtocoloException;
import sdis.broker.common.MensajeProtocolo;
import sdis.broker.common.Primitiva;

public class DeleteQ {
    //Ejemplo
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
            }else {
                System.out.println(minfo);
            }
            if(args.length== 0 && !fin) {
               	pruebaPeticionRespuesta(new MensajeProtocolo(Primitiva.DELETEQ, "Bienvenida"));
               	MensajeProtocolo mr = (MensajeProtocolo) ois.readObject();
               	System.out.println("< " + mr);
            }else if(args.length== 1 && !fin) {
               	String conversacion = args[0];
        		pruebaPeticionRespuesta(new MensajeProtocolo(Primitiva.DELETEQ, conversacion));
        		MensajeProtocolo mr = (MensajeProtocolo) ois.readObject();
               	System.out.println("< " + mr);
            	
            }else {
            	System.out.println("Es posible que haya habido un fallo de conexion. Pruebe mas tarde o no introduzca argumentos o introduzca: ConversacionBorrar ");
            }
            
            
        } catch (Exception e) {
            System.err.println("<Cliente: Excepcion: " + e);
            e.printStackTrace();

        }finally {
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
