package servidorcursosudp;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Elián
 */
public class GestorCursos
{
    // <editor-fold desc="Atributos" defaultstate="collapsed">
    public ArrayList<String> cursos = new ArrayList<>();
    public ArrayList<String> alumnos = new ArrayList<>();
    public HashMap<String, ArrayList<String>> alumnosPorCurso = new HashMap<>();
    // </editor-fold>

    // <editor-fold desc="Métodos" defaultstate="collapsed">
    public boolean nuevoCurso(String curso)
    {
        if (!existeCurso(curso))
        {
            cursos.add(curso);
            alumnosPorCurso.put(curso, new ArrayList<String>());

            return false;
        }
        return true; // Devuelve verdad si existe el curso
    }

    public boolean nuevoAlumno(String alumno)
    {
        if (!existeAlumno(alumno))
        {
            alumnos.add(alumno);

            return false;
        }
        return true; // Devuelve verdad si existe el alumno
    }

    public void matricular(String curso, String alumno)
    {
        if (curso == null)
        {
            alumnos.add(alumno);
            return;
        }
        if (alumnos.contains(alumno) && !alumnosPorCurso.get(curso).contains(alumno))
        {
            // Si el alumno existe pero no tiene curso se añade al HashMap
            alumnosPorCurso.get(curso).add(alumno);
        }
        if (!alumnos.contains(alumno))
        {
            alumnos.add(alumno);

            if (!cursos.contains(curso))
            {
                alumnosPorCurso.put(curso, new ArrayList<String>());
            }
            alumnosPorCurso.get(curso).add(alumno);
        }
    }

    public String getListaAlumnos()
    {
        String lista = "";
        for (var item : alumnos)
        {
            lista += item + "\n";
        }
        return lista;
    }

    public String getListaCursos()
    {
        String lista = "";

        for (var item : cursos)
        {
            lista += item + "\n";
        }
        return lista;
    }

    public boolean estaAlumnoMatriculado(String alumno, String curso)
    {
        return alumnosPorCurso.get(curso).contains(alumno);
    }

    public boolean existeAlumno(String alumno)
    {
        return alumnos.contains(alumno);
    }

    public boolean existeCurso(String curso)
    {
        return cursos.contains(curso);
    }
    // </editor-fold>
}
