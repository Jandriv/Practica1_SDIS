package sdis.brokermsg.tcp.common;

public class MensajeProtocolo implements java.io.Serializable {
    private final Primitiva primitiva;
    private final String mensaje;
    private final String idCola;

    private static final long serialVersionUID = 2540712323068668552L;
    // ADDED, EMPTY, DELETED
    public MensajeProtocolo(Primitiva p) throws MalMensajeProtocoloException {
        if (p == Primitiva.ADDED || p == Primitiva.EMPTY || p == Primitiva.DELETED || p == Primitiva.STATE) {
            this.primitiva = p;
            this.mensaje = this.idCola = null;
        } else {
            throw new MalMensajeProtocoloException();
        }
    }

    // XAUTH, MEDIA, ERROR, NOTAUTH, INFO, READQ, STATE, DELETEQ, BADCODE
    public MensajeProtocolo(Primitiva p, String mensaje) throws MalMensajeProtocoloException {
        if (p == Primitiva.INFO || p == Primitiva.XAUTH || p == Primitiva.MSG || p == Primitiva.ERROR || p == Primitiva.NOTAUTH || p == Primitiva.STATE || p == Primitiva.BADCODE) {
            this.mensaje = mensaje;
            this.idCola = null;
        } else if (p == Primitiva.READQ || p == Primitiva.DELETEQ) {
            this.mensaje = null;
            this.idCola = mensaje;
        } else{
            throw new MalMensajeProtocoloException();

        }
        this.primitiva = p;
    }
    // XAUTH, ADDMSG
    public MensajeProtocolo(Primitiva p, String user, String pass) throws MalMensajeProtocoloException {
        if (p == Primitiva.XAUTH || p == Primitiva.ADDMSG) {

            this.idCola = user;
            this.mensaje = pass;
            this.primitiva = p;
        } else {
            throw new MalMensajeProtocoloException();
        }
    }

    public Primitiva getPrimitiva() {
        return this.primitiva;
    }

    public String getMensaje() {
        return this.mensaje;
    }

    public String getIdCola() {
        return this.idCola;
    }

    public String toString() {
        switch (this.primitiva) {
            case INFO:
                return this.mensaje;
            case ADDED:
                return this.primitiva.toString();
            case EMPTY:
                return this.primitiva.toString();
            case DELETED:
                return this.primitiva.toString();
            case XAUTH:
                if (this.idCola != null){
                    return this.primitiva + ": " + this.idCola + ": " + this.mensaje;
                }else {
                    return this.primitiva + ": " + this.mensaje;
                }

            case MSG:
                return this.primitiva + ": " + this.mensaje;
            case ERROR:
            case NOTAUTH:
                return this.primitiva + ": " + this.mensaje;
            case READQ:
                return this.primitiva+": "+this.idCola;
            case DELETEQ:
                return this.primitiva + ":" + this.idCola;
            case ADDMSG:
                return this.primitiva + ": Conversacion: " + this.idCola + " mensaje: " + this.mensaje;
            case STATE:
                if(this.mensaje == null){
                    return this.primitiva.toString();
                }
                return this.primitiva + ": " + this.mensaje;
            default:
                return this.primitiva.toString();
        }
    }
}