package sdis.broker.client;


import sdis.broker.common.*;

public class ClientesInteractivos {
    final private int PUERTO = 2000;
    static java.io.ObjectInputStream ois = null;
    static java.io.ObjectOutputStream oos = null;
    public static void main(String[] args) throws java.io.IOException {
        String linea = null;
        boolean fin = false;
        java.net.Socket sock = new java.net.Socket("localhost", 3000);
        try {
            java.io.BufferedReader tec =
                    new java.io.BufferedReader(
                            new java.io.InputStreamReader(System.in));
            //Creación de los canales de serialización de objetos
            System.out.println("Esperando para conectarse con el servidor...");
            oos = new java.io.ObjectOutputStream(sock.getOutputStream());
            ois = new java.io.ObjectInputStream(sock.getInputStream());
            MensajeProtocolo minfo = (MensajeProtocolo) ois.readObject();
            if (minfo.getPrimitiva().equals(Primitiva.ERROR)){
                System.out.println(minfo);
                fin = true;
            }else{
                System.out.println(minfo);
            }
            while(!fin){
                System.out.println("Que desea hacer: 1-Loggear 2-Enviar Mensaje 3-Leer Mensajes 4-Borrar Mensajes 5-Admin Info");
                linea = tec.readLine();
                if(linea.equals("1")){
                  System.out.println("Usuario: ");
                  String Usuario = tec.readLine();
                  System.out.println("Contraseña: ");
                  String Password = tec.readLine();
                  pruebaPeticionRespuesta(new MensajeProtocolo(Primitiva.XAUTH, Usuario, Password));
                  MensajeProtocolo mr = (MensajeProtocolo) ois.readObject();
                  if(mr.getPrimitiva().equals(Primitiva.ERROR)){
                      fin = true;
                  }
                    System.out.println("< " +mr);
                }
                else if (linea.equals("2")){
                    System.out.println("Conversacion: ");
                    String PlayList = tec.readLine();
                    System.out.println("Mensaje: ");
                    String Song = tec.readLine();
                    pruebaPeticionRespuesta(new MensajeProtocolo(Primitiva.ADDMSG, PlayList, Song));
                    MensajeProtocolo mr = (MensajeProtocolo) ois.readObject();
                    System.out.println("< " +mr);
                }
                else if(linea.equals("3")){
                    System.out.println("Conversacion: ");
                    String PlayList = tec.readLine();
                    pruebaPeticionRespuesta(new MensajeProtocolo(Primitiva.READQ, PlayList));
                    MensajeProtocolo mr = (MensajeProtocolo) ois.readObject();
                    System.out.println("< " +mr);
                }
                else if(linea.equals("4")){
                    System.out.println("Conversacion: ");
                    String PlayList = tec.readLine();
                    pruebaPeticionRespuesta(new MensajeProtocolo(Primitiva.DELETEQ, PlayList));
                    MensajeProtocolo mr = (MensajeProtocolo) ois.readObject();
                    System.out.println("< " +mr);
                }
                else if(linea.equals("5")) {
                    pruebaPeticionRespuesta(new MensajeProtocolo(Primitiva.STATE));
                    MensajeProtocolo mr = (MensajeProtocolo) ois.readObject();
                    System.out.println("< " + mr);
                }else{
                    System.out.println("Elija una opción valida");
                }

            }
            //a estas alturas algún cliente externo debería insertar un mensaje en la cola
            //FIN Esceniario 1
        } catch (java.io.EOFException e) {
            System.err.println("Cliente: Fin de conexión.");
        } catch (java.io.IOException e) {
            System.err.println("Cliente: Error de apertura o E/S sobre objetos: "+e);
        } catch (MalMensajeProtocoloException e) {
            System.err.println("Cliente: Error mensaje Protocolo: "+e);
        } catch (Exception e) {
            System.err.println("Cliente: Excepción. Cerrando Sockets: "+e);
        } finally {
            ois.close();
            oos.close();
            sock.close();
        }
    }
    //Prueba una interacción de escritura y lectura con el servidor
    static void pruebaPeticionRespuesta(MensajeProtocolo mp) throws java.io.IOException, MalMensajeProtocoloException, ClassNotFoundException {
        System.out.println("> "+mp);
        oos.writeObject(mp);

    }
}


