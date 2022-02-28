package noExamen;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import servidorcursosudp.GestorCursos;

/**
 *
 * @author Elián
 */
public class ComandosCursos
{
    //<editor-fold desc="Atributos" defaultstate="collapsed"> 
    private static Matcher matcher;
    private static final String STR_PAT_LETRAS_NUMEROS = "[a-zA-Z0-9]+";

    private static final Pattern patNuevoCurso = Pattern.compile("@nuevocur#(" + STR_PAT_LETRAS_NUMEROS + ")@");
    private static final Pattern patNuevoAl = Pattern.compile("@nuevoal#(" + STR_PAT_LETRAS_NUMEROS + ")@");
    private static final Pattern patMatricCursoAlumno = Pattern.compile("@matric#(" + STR_PAT_LETRAS_NUMEROS + ")#(" + STR_PAT_LETRAS_NUMEROS + ")@");

    private static final String LISTA_CURSOS = "@?cur@";
    private static final String LISTA_ALUMNOS = "@?al@";
    //</editor-fold>
    
    //<editor-fold desc="Métodos" defaultstate="collapsed">
    static String obtenerRespuestaSegunMensajeRecibido(String mensajeRecibido, GestorCursos gestorCursos)
    {
        // <editor-fold desc="Nuevo curso" defaultstate="collapsed">
        if ((matcher = patNuevoCurso.matcher(mensajeRecibido)).matches())
        {
            var curso = matcher.group(1);
            var existeCurso = gestorCursos.nuevoCurso(curso);

            if (existeCurso) return "WARN EXISTECUR " + curso;
            else return "OK CUR " + curso;
        }
        // </editor-fold>

        // <editor-fold desc="Nuevo alumno" defaultstate="collapsed">
        else if ((matcher = patNuevoAl.matcher(mensajeRecibido)).matches())
        {
            var alumno = matcher.group(1);
            var existeAlumno = gestorCursos.nuevoAlumno(alumno);
            
            if (existeAlumno) return "WARN EXISTEAL " + alumno;
            else return "OK AL " + alumno;
        }
        // </editor-fold>

        // <editor-fold desc="Matricular" defaultstate="collapsed">
        else if ((matcher = patMatricCursoAlumno.matcher(mensajeRecibido)).matches())
        {
            var curso = matcher.group(1);
            var alumno = matcher.group(2);

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

        // <editor-fold desc="Consultar lista cursos" defaultstate="collapsed">
        else if (mensajeRecibido.equals(LISTA_CURSOS))
        {
            return gestorCursos.getListaCursos();
        }
        // </editor-fold>
        
        // <editor-fold desc="Consultar lista alumnos" defaultstate="collapsed">
        else if (mensajeRecibido.equals(LISTA_ALUMNOS))
        {
            return gestorCursos.getListaAlumnos();
        }
        // </editor-fold>

        // <editor-fold desc="Error" defaultstate="collapsed">
        else return "@ERR: comando incorrecto: " + mensajeRecibido;
        // </editor-fold>
    }
    //</editor-fold>
}
