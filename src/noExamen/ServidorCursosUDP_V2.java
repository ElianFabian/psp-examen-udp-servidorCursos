package noExamen;

import servidorcursosudp.GestorCursos;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 *
 * @author elian
 *
 * Versión realizada a parte, no forma parte del examen original.
 */
public class ServidorCursosUDP_V2
{
    static private final int NUM_PUERTO = 8450;
    static private final int MAX_BYTES = 1400;
    static private final String COD_TEXTO = "UTF-8";

    static private final GestorCursos gestorCursos = new GestorCursos();

    public static void main(String[] args)
    {
        System.out.println("--- SERVIDOR V2 ---");
        
        try ( var serverSocket = new DatagramSocket(NUM_PUERTO))
        {
            var gestorUDP = new GestorUDPServidor(serverSocket, MAX_BYTES, COD_TEXTO);

            while (true)
            {
                gestorUDP.recibirMensaje();

                gestorUDP.mensajeRespuesta = ComandosCursos.obtenerRespuestaSegunMensajeRecibido(gestorUDP.mensajeRecibido, gestorCursos);
                
                gestorUDP.enviarMensaje();
            }
        }
        catch (SocketException e)
        {
        }
    }
}
