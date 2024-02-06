package org.example.utilities;

import org.example.modelos.Categorias;
import org.example.modelos.Empleado;
import org.example.modelos.EmpleadoProyecto;
import org.example.modelos.Proyecto;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class Validaciones{
    private static final Pattern REGEXP_DNI = Pattern.compile("[0-9]{8}[A-Z]");
    private static final Pattern REGEXP_NOMBRE = Pattern.compile("[a-zA-Z\\säÄëËïÏöÖüÜáéíóúáéíóúÁÉÍÓÚÂÊÎÔÛâêîôûàèìòùÀÈÌÒÙ]{1,35}");

    private static final Pattern REGEXP_NOMBRE_PROYECTO = Pattern.compile("[a-zA-Z0-9\\säÄëËïÏöÖüÜáéíóúáéíóúÁÉÍÓÚÂÊÎÔÛâêîôûàèìòùÀÈÌÒÙ]{1,35}");
    private static final Pattern REGEXP_SUELDO = Pattern.compile("^[0-9.]+([0-9]+){0,1}$");
    private static final String DIGITO_CONTROL = "TRWAGMYFPDXBNJZSQVHLCKE";
    private static final String[] INVALIDOS = new String[] { "00000000T", "00000001R", "99999999R" };

    public static boolean validarDNI(String dni) {
        return Arrays.binarySearch(INVALIDOS, dni) < 0 // (1)
                && REGEXP_DNI.matcher(dni).matches() // (2)
                && dni.charAt(8) == DIGITO_CONTROL.charAt(Integer.parseInt(dni.substring(0, 8)) % 23); // (3)
    }

    public static boolean validarNombre(String nombre){
        return REGEXP_NOMBRE.matcher(nombre).matches();
    }

    public static boolean validarNombreProyecto(String nombre){
        return REGEXP_NOMBRE_PROYECTO.matcher(nombre).matches();
    }

    public static boolean comprobarSueldo(String sueldo){
        return REGEXP_SUELDO.matcher(sueldo).matches();
    }

    public static boolean comprobarCategoria(String c){
        for (Categorias cat: Categorias.values()) {
            if (c.equals(String.valueOf(cat))){
                return true;
            }
        }
        return false;
    }

    public static boolean comprobarFechaFinMayorFechaIni(LocalDate fechaIni,LocalDate fechaFin){
        return fechaFin.isAfter(fechaIni);
    }

    public static boolean comprobarEmpleadoEstaEnProyecto(Empleado empleado, EmpleadoProyecto asignacion){
        for (int x = empleado.getListaEmpleadosProyectos().size() -1; x >= 0; x--){
            if (empleado.getListaEmpleadosProyectos().get(x).getProyecto().getIdProyecto() == asignacion.getProyecto().getIdProyecto()){
                return true;
            }
        }
        return false;
    }



    public static boolean comprobarSiUnEmpleadoEsJefeDeUnProyecto(Empleado empleado, Proyecto proyecto){
        for(int x = empleado.getProyectos().size() -1; x >= 0; x--){
            if (empleado.getDni().equals(proyecto.getJefe().getDni())){
                return true;
            }
        }
        return false;
    }

}
