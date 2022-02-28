package noExamen;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Elián
 */
public class GestorUDPServidor
{
    // <editor-fold desc="Atributos" defaultstate="collapsed">
    public DatagramSocket datagramSocket;
    public String mensajeRecibido;
    public String mensajeRespuesta;

    public InetAddress IPCliente;
    public int puertoCliente;

    public int max_bytes;
    public String cod_texto = "UTF-8";
    // </editor-fold>

    // <editor-fold desc="Constructores" defaultstate="collapsed">
    public GestorUDPServidor(DatagramSocket datagramSocket, int max_bytes, String cod_texto)
    {
        this.datagramSocket = datagramSocket;
        this.max_bytes = max_bytes;
        this.cod_texto = cod_texto;
    }

    public GestorUDPServidor(DatagramSocket datagramSocket, int max_bytes)
    {
        this.datagramSocket = datagramSocket;
        this.max_bytes = max_bytes;
    }
    // </editor-fold>

    // <editor-fold desc="Métodos" defaultstate="collapsed">
    public void recibirMensaje()
    {
        try
        {
            byte[] datosRecibidos = new byte[max_bytes];
            var paqueteRecibido = new DatagramPacket(datosRecibidos, datosRecibidos.length);

            datagramSocket.receive(paqueteRecibido);

            mensajeRecibido = new String(
                    paqueteRecibido.getData(),
                    0,
                    paqueteRecibido.getLength(),
                    cod_texto
            );

            IPCliente = paqueteRecibido.getAddress();
            puertoCliente = paqueteRecibido.getPort();
        }
        // <editor-fold desc="Catch" defaultstate="collapsed">
        catch (IOException ex)
        {
            Logger.getLogger(GestorUDPServidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        // </editor-fold>
    }

    public void enviarMensaje()
    {
        try
        {
            byte[] b = mensajeRespuesta.getBytes(cod_texto);

            var paqueteEnviado = new DatagramPacket(
                    b,
                    b.length,
                    IPCliente,
                    puertoCliente
            );
            datagramSocket.send(paqueteEnviado);
        }
        // <editor-fold desc="Catch" defaultstate="collapsed">
        catch (UnsupportedEncodingException ex)
        {
            Logger.getLogger(GestorUDPServidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            Logger.getLogger(GestorUDPServidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        // </editor-fold>
    }
    // </editor-fold>
}
