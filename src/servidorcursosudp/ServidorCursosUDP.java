package servidorcursosudp;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author elian
 */
public class ServidorCursosUDP
{
    static private final int NUM_PUERTO = 8450;
    static private final int MAX_BYTES = 1400;
    static private final String COD_TEXTO = "UTF-8";

    static private final GestorCursos gestorCursos = new GestorCursos();

    public static void main(String[] args)
    {
        System.out.println("---SERVIDOR---");
        try ( var serverSocket = new DatagramSocket(NUM_PUERTO))
        {
            while (true)
            {
                System.out.println("Esperando datagramas.");

                byte[] datosRecibidos = new byte[MAX_BYTES];
                var paqueteRecibido = new DatagramPacket(datosRecibidos, datosRecibidos.length);

                serverSocket.receive(paqueteRecibido);

                String mensajeRecibido = new String(
                        paqueteRecibido.getData(),
                        0,
                        paqueteRecibido.getLength(),
                        COD_TEXTO
                );

                InetAddress IPCliente = paqueteRecibido.getAddress();
                int puertoCliente = paqueteRecibido.getPort();

                String mensajeRespuesta = obtenerRespuestaSegunMensajeRecibido(mensajeRecibido);

                byte[] b = mensajeRespuesta.getBytes(COD_TEXTO);
                System.out.println(mensajeRespuesta);

                var paqueteEnviado = new DatagramPacket(
                        b,
                        b.length,
                        IPCliente,
                        puertoCliente
                );
                serverSocket.send(paqueteEnviado);
            }
        }
        // <editor-fold desc="Catch" defaultstate="collapsed">
        catch (SocketException e)
        {
        }
        catch (UnsupportedEncodingException ex)
        {
            Logger.getLogger(ServidorCursosUDP.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            Logger.getLogger(ServidorCursosUDP.class.getName()).log(Level.SEVERE, null, ex);
        }
        // </editor-fold>
    }

    static String obtenerRespuestaSegunMensajeRecibido(String mensajeRecibido)
    {
        final var strPatLetrasNumeros = "[a-zA-Z0-9]+";

        var patNuevoCurso = Pattern.compile("@nuevocur#(" + strPatLetrasNumeros + ")@");
        var patNuevoAl = Pattern.compile("@nuevoal#(" + strPatLetrasNumeros + ")@");
        var patMatricCursoAlumno = Pattern.compile("@matric#(" + strPatLetrasNumeros + ")#(" + strPatLetrasNumeros + ")@");

        Matcher m = null;

        // <editor-fold desc="Nuevo curso" defaultstate="collapsed">
        if ((m = patNuevoCurso.matcher(mensajeRecibido)).matches())
        {
            var curso = m.group(1);
            var existeCurso = gestorCursos.nuevoCurso(curso);
            if (existeCurso)
            {
                return "WARN EXISTECUR " + curso;
            }
            else
            {
                return "OK CUR " + curso;
            }
        }
        // </editor-fold>

        // <editor-fold desc="Nuevo alumno" defaultstate="collapsed">
        else if ((m = patNuevoAl.matcher(mensajeRecibido)).matches())
        {
            var alumno = m.group(1);
            var existeAlumno = gestorCursos.nuevoAlumno(alumno);
            if (existeAlumno)
            {
                return "WARN EXISTEAL " + alumno;
            }
            else
            {
                return "OK AL " + alumno;
            }
        }
        // </editor-fold>

        // <editor-fold desc="Matricular" defaultstate="collapsed">
        else if ((m = patMatricCursoAlumno.matcher(mensajeRecibido)).matches())
        {
            var curso = m.group(1);
            var alumno = m.group(2);

            if (!gestorCursos.cursos.contains(curso))
            {
                return "ERR: NOCUR " + curso;
            }
            else if (!gestorCursos.alumnos.contains(alumno))
            {
                return "ERR: NOAL " + alumno;
            }

            var alumnoMatriculadoEnCurso = gestorCursos.alumnosPorCurso.get(curso).contains(alumno);
            if (alumnoMatriculadoEnCurso)
            {
                return "WARN: YAMAT " + alumno + " " + curso;
            }
            gestorCursos.matricular(curso, alumno);
            return "OK MATRIC " + alumno + " " + curso;
        }
        // </editor-fold>

        // <editor-fold desc="Consultar lista alumnos" defaultstate="collapsed">
        else if (mensajeRecibido.equals("@?al@"))
        {            
            return gestorCursos.getListaAlumnos(); 
        }
        // </editor-fold>

        // <editor-fold desc="Consultar lista cursos" defaultstate="collapsed">
        else if (mensajeRecibido.equals("@?cur@"))
        {            
            return gestorCursos.getListaCursos();
        }
        // </editor-fold>

        // <editor-fold desc="Error" defaultstate="collapsed">
        else
        {
            return "@ERR: comando incorrecto: " + mensajeRecibido;
        }
        // </editor-fold>
    }
}
