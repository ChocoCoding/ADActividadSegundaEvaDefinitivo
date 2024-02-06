package org.example.view;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.NoResultException;
import org.example.modelos.*;
import org.example.repositorios.DatosProfesionalesRepository;
import org.example.repositorios.EmpleadoProyectoRepository;
import org.example.repositorios.EmpleadoRepository;
import org.example.repositorios.ProyectoRepository;
import org.example.utilities.Utilidades;
import org.example.utilities.Validaciones;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.DataException;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Menu {
    Session session;
    Scanner sc = new Scanner(System.in);

    public Menu(Session session) {
    this.session = session;
    }

    private EmpleadoRepository empleadoRepository;
    private DatosProfesionalesRepository datosProfesionalesRepository;
    private EmpleadoProyectoRepository empleadoProyectoRepository;
    private ProyectoRepository proyectoRepository;


    public void dialog(){

    Scanner scanner = new Scanner(System.in);
    empleadoRepository = new EmpleadoRepository(session);
    datosProfesionalesRepository = new DatosProfesionalesRepository(session);
    empleadoProyectoRepository = new EmpleadoProyectoRepository(session);
    proyectoRepository = new ProyectoRepository(session);

    try {
        int opt = 1;
        do {
            System.out.println("\n********************** Bienvenido a Empleados-Proyectos *****************************");
            System.out.println("\n\t1. Gestionar Empleados.\t\t\t\t\t2. Gestionar Proyectos.");
            System.out.println("\n\t0. Salir.");
            System.out.println("\n*************************************************************************************");
            scanner = new Scanner(System.in);
            System.out.println("Introduce un opcion");
            opt = scanner.nextInt();

            switch (opt){
                case 1:
                    gestionarEmpleados();
                    break;
                case 2:
                    gestionarProyectos();
                    break;
                default:
                    System.out.println("La opcion no existe, los valores deben ser del [0 - 3]");
                    break;
            }
        } while (opt != 0);
    }catch (InputMismatchException ime){
        System.out.println("ERROR!! Los valores deben ser del [0 - 3]");
        dialog();
    }


}

    private void gestionarProyectos() {
        try {
            Scanner scanner = new Scanner(System.in);
            int opt = 1;
            do {
                System.out.println("\n********************** Gestión de Proyectos *****************************");
                System.out.println("\n\t1. Crear Proyecto.\t\t\t\t\t\t4. Cambiar el jefe de un proyecto.");
                System.out.println("\n\t2. Modificar Proyecto.\t\t\t\t\t5. Mostrar los datos de un proyecto.");
                System.out.println("\n\t3. Eliminar Proyecto.\t\t\t\t\t0. Salir.");
                System.out.println("\n*************************************************************************************");
                scanner = new Scanner(System.in);
                System.out.println("Introduce un opcion");
                opt = scanner.nextInt();

                switch (opt){
                    case 1:
                        crearProyecto();
                        break;
                    case 2:
                        modificarProyecto();
                        break;
                    case 3:
                        eliminarProyecto();
                        break;
                    case 4:
                        cambiarJefeProyecto();
                        break;
                    case 5:
                        mostrarDatosProyecto();
                        break;
                    default:
                        System.out.println("La opcion no existe, los valores deben ser del [0 - 5]");
                        break;
                }
            } while (opt != 0);
        }catch (InputMismatchException ime){
            System.out.println("ERROR!! Los valores deben ser del [0 - 5]");
            gestionarProyectos();
        }

    }

    private void mostrarDatosProyecto() {
        try {
            Proyecto proyecto = proyectoRepository.findProyectoByid(Utilidades.pedirInt("Introduce el ID del proyecto a mostrar"));
            System.out.println("Datos proyecto");
            System.out.println(proyecto.toString());
            System.out.println();

            //Mostramos los empleados asignados:
            System.out.println("Empleados: ");
            for (EmpleadoProyecto ep: proyecto.getListaEmpleadosProyectos()){
                System.out.println("DNI empleado: "+ ep.getEmpleado().getNombre());
                System.out.println("DNI empleado: "+ ep.getEmpleado().getDni());
                System.out.println("Fecha inicio empleado: "+ ep.getFechaInicio());
                System.out.println("Fecha fin empleado: "+ ep.getFechaFin());
            }

            System.out.println("Jefe de proyecto: ");
            System.out.println("Nombre: "+ proyecto.getJefe().getNombre());
            System.out.println("DNI Jefe: "+ proyecto.getJefe().getDni());
            System.out.println(proyecto.getJefe().getDatosProfesionales());
            System.out.println();
        }catch (NullPointerException nre){
            System.out.println("El proyecto seleccionado no existe");
        }


    }

    private void modificarProyecto() {
        try {
            Proyecto proyecto = proyectoRepository.findProyectoByid(Utilidades.pedirInt("Introduce el ID del proyecto a modificar"));
            LocalDate fechaFin;
            String nombre;

            do {
                nombre = Utilidades.pedirString("Introduce el nuevo nombre del proyecto");
                sc = new Scanner(System.in);
                if (!Validaciones.validarNombreProyecto(nombre)){
                    System.out.println("-Máximo 35 caracteres");
                    System.out.println("-El nombre debe contener al menos 1 letra");
                }
            }while (!Validaciones.validarNombreProyecto(nombre));


            LocalDate fechaInicio = Utilidades.parsearFecha(Utilidades.pedirPalabra("Introduce la nueva fecha de inicio"));
            do{
                fechaFin = Utilidades.parsearFecha(Utilidades.pedirPalabra("Introduce la fecha de finalizacion en el siguiente formato: DD-MM-YYYY"));
                if (!Validaciones.comprobarFechaFinMayorFechaIni(fechaInicio,fechaFin)){
                    System.out.println("La fecha fin debe ser mayor que la fecha de inicio");
                }
            }while(!Validaciones.comprobarFechaFinMayorFechaIni(fechaInicio,fechaFin));

            proyecto.setNombre(nombre);
            proyecto.setFechaInicio(fechaInicio);
            proyecto.setFechaFin(fechaFin);
            proyectoRepository.update(proyecto);
        }catch (NullPointerException nre){
            System.out.println("El proyecto no existe");
        }catch (InputMismatchException ime){
            System.out.println("El dato introducido no es correcto");
        }catch (DateTimeParseException dtpe){
            System.out.println("El formato de fecha debe ser: [dd-mm-YYYY]");
        }


    }

    private void crearProyecto() {
        LocalDate fechaFin;
        String nombre;
        try {
            do{
                nombre = Utilidades.pedirString("Introduce el nombre del proyecto");
                sc = new Scanner(System.in);
            if (!Validaciones.validarNombreProyecto(nombre)){
                System.out.println("-Máximo 35 caracteres");
                System.out.println("-El nombre debe contener al menos 1 letra");
            }
        }while (!Validaciones.validarNombreProyecto(nombre));
            LocalDate fechaInicio = Utilidades.parsearFecha(Utilidades.pedirPalabra("Introduce la fecha de inicio en el siguiente formato: DD-MM-YYYY"));
            do{
                fechaFin = Utilidades.parsearFecha(Utilidades.pedirPalabra("Introduce la fecha de finalizacion en el siguiente formato: DD-MM-YYYY"));
                if (!Validaciones.comprobarFechaFinMayorFechaIni(fechaInicio,fechaFin)){
                    System.out.println("La fecha fin debe ser mayor que la fecha de inicio");
                }
            }while(!Validaciones.comprobarFechaFinMayorFechaIni(fechaInicio,fechaFin));

            Empleado jefe;
            do {
                jefe = empleadoRepository.findEmpleadoByDni(Utilidades.pedirPalabra("Introduce el DNI del jefe del proyecto"));

                if (jefe.getDatosProfesionales() == null){
                    System.out.println("Solo los empleados de plantilla pueden ser jefes");
                }
            }while(jefe.getDatosProfesionales() == null);

            Proyecto proyecto = new Proyecto(nombre, fechaInicio, fechaFin, jefe);
            EmpleadoProyecto empleadoProyecto = new EmpleadoProyecto(jefe, proyecto, fechaInicio, fechaFin);
            proyecto.asignarEmpleadosProyecto(empleadoProyecto);
            jefe.addlistEmpleadosProyectos(empleadoProyecto);
            jefe.addProyecto(proyecto);

            proyectoRepository.crear(proyecto);
            empleadoProyectoRepository.crear(empleadoProyecto);
        }catch (DateTimeParseException dtpe){
            System.out.println("La fecha es incorrecta, debe estar en formato [DD-MM-YYYY]");
        }catch (DataException de){
            System.out.println("El nombre del proyecto no debe exceder los 35 caracteres");
        }catch (NullPointerException nre){
            System.out.println("No existe el empleado con ese DNI");
        }catch (InputMismatchException ime){
            System.out.println("El dato introducido no es correcto");
        }
    }

    private void eliminarProyecto(){
        try {
        int id = Utilidades.pedirInt("Introduce el id del proyecto a eliminar");

        Proyecto proyecto = proyectoRepository.findProyectoByid(id);
        //Asignaciones lado proyectos
        List<EmpleadoProyecto> asignacionesLadoProyecto = proyecto.getListaEmpleadosProyectos();

        //Asignacion lado empleado
        List<EmpleadoProyecto> asignacionesLadoEmpleado = proyecto.getListaEmpleadosProyectos();

        //Asignaciones
        List<EmpleadoProyecto> asignacionesDelProyecto = empleadoProyectoRepository.findAsignacionesById(id);

        //Lista de proyectos en las que es jefe del proyecto
        Empleado jefe = proyecto.getJefe();


        for (int x = asignacionesLadoProyecto.size() -1; x >= 0; x--){
            proyecto.getListaEmpleadosProyectos().remove(asignacionesDelProyecto.get(x));
            empleadoProyectoRepository.remove(asignacionesDelProyecto.get(x));
        }

        for (int x = asignacionesLadoEmpleado.size() -1; x >= 0; x--){
            if (asignacionesLadoEmpleado.get(x).getProyecto().getIdProyecto() == id){
                proyecto.getJefe().getListaEmpleadosProyectos().remove(asignacionesLadoEmpleado.get(x));
                empleadoProyectoRepository.remove(asignacionesLadoEmpleado.get(x));
            }
        }

        //Desvinculamos el proyecto del jefe
        jefe.getProyectos().remove(proyecto);
        empleadoRepository.update(jefe);

        proyectoRepository.remove(proyecto);
        }catch (InputMismatchException ime){
            System.out.println("El dato introducido no es correcto");
        }catch (NullPointerException nre){
            System.out.println("No existe el proyecto");
        }
    }

    public void cambiarJefeProyecto(){

        try {
        int id = Utilidades.pedirInt("Introduce la id del proyecto");
        Proyecto proyecto = proyectoRepository.findProyectoByid(id);
        Empleado jefe = proyecto.getJefe();
        String dni = Utilidades.pedirPalabra("Introduce el DNI del nuevo jefe");
        Empleado empleado = empleadoRepository.findEmpleadoByDni(dni);

        //Eliminamos las asignaciones al jefe
        for (int x = jefe.getListaEmpleadosProyectos().size() -1; x >= 0; x--){
            if (jefe.getListaEmpleadosProyectos().get(x).getEmpleado().getDni().equals(jefe.getDni()) && jefe.getListaEmpleadosProyectos().get(x).getProyecto().getIdProyecto() == (proyecto.getIdProyecto())){
                //empleado.getListaEmpleadosProyectos().remove(jefe.getListaEmpleadosProyectos().get(x));
                empleadoProyectoRepository.remove(jefe.getListaEmpleadosProyectos().get(x));
            }
        }

        //Eliminamos las asignaciones al proyecto
        for (int x = proyecto.getListaEmpleadosProyectos().size() -1; x >= 0; x--){
            if (proyecto.getListaEmpleadosProyectos().get(x).getEmpleado().getDni().equals(jefe.getNombre()) && proyecto.getListaEmpleadosProyectos().get(x).getProyecto().getIdProyecto() == (proyecto.getIdProyecto())){
                //proyecto.getListaEmpleadosProyectos().remove(proyecto.getListaEmpleadosProyectos().get(x));
                empleadoProyectoRepository.remove(proyecto.getListaEmpleadosProyectos().get(x));
            }
        }

        //Creamos la nueva asignacion
        EmpleadoProyecto empleadoProyecto = new EmpleadoProyecto(empleado,proyecto,proyecto.getFechaInicio(),proyecto.getFechaFin());
        EmpleadoProyecto empleadoProyecto1 = empleadoProyectoRepository.findAsignacionEmpleadoProyecto(empleado.getDni(),proyecto.getIdProyecto());
        if (!empleadoProyecto1.getEmpleado().getDni().equals(empleadoProyecto.getEmpleado().getDni()) && empleadoProyecto1.getProyecto().getIdProyecto() == empleadoProyecto.getProyecto().getIdProyecto()){
            empleado.addlistEmpleadosProyectos(empleadoProyecto);
            proyecto.asignarEmpleadosProyecto(empleadoProyecto);
            empleadoProyectoRepository.crear(empleadoProyecto);
        }

        //Cambiamos al jefe de proyecto
        jefe.getProyectos().remove(proyecto);
        proyecto.setJefe(empleado);
        empleadoRepository.update(empleado);
        proyectoRepository.update(proyecto);

        }catch (InputMismatchException ime){
            System.out.println("El dato introducido no es correcto");
        }catch (NullPointerException nre){
            System.out.println("ERROR,puede ser por las siguientes razones: ");
            System.out.println("No existe el empleado");
            System.out.println("No existe el proyecto");
        }

    }

    public void gestionarEmpleados(){
        try {
            Scanner scanner = new Scanner(System.in);
            int opt = 1;
            do {
                System.out.println("\n************************************ Gestión de empleados ****************************************");
                System.out.println("\n\t1. Crear Empleado.\t\t\t\t\t\t5. Quitar un empleado de un proyecto.");
                System.out.println("\n\t2. Modificar Empleado.\t\t\t\t\t6. Mostrar datos empleados en plantilla.");
                System.out.println("\n\t3. Eliminar Empleado.\t\t\t\t\t7. Mostrar los Jefes de Proyecto.");
                System.out.println("\n\t4. Asignar Empleado a proyecto.\t\t\t0. SALIR.");
                System.out.println("\n***************************************************************************************************");
                scanner = new Scanner(System.in);
                System.out.println("Introduce un opcion");
                opt = scanner.nextInt();

                switch (opt){
                    case 1:
                        crearEmpleadoOEmpleadoPlantilla();
                        break;
                    case 2:
                        modificarEmpleado();
                        break;
                    case 3:
                        borrarEmpleado();
                        break;
                    case 4:
                        asignarEmpleadoAProyecto();
                        break;
                    case 5:
                        quitarEmpleadoDeProyecto();
                        break;
                    case 6:
                        mostrarDatosEmpleadosPlantilla();
                        break;
                    case 7:
                        mostrarEmpleadosJefes();
                        break;
                    default:
                        System.out.println("La opcion no existe, los valores deben ser del [0 - 7]");
                        break;
                }

            } while (opt != 0);
        }catch (InputMismatchException ime){
            System.out.println("ERROR!! Los valores deben ser del [0 - 7]");
            gestionarEmpleados();
        }
    }

    private void mostrarEmpleadosJefes() {
        List<Empleado> empleados = empleadoRepository.findAll();
        for(Empleado e: empleados){
                if (!e.getProyectos().isEmpty()){
                    for (int x = e.getProyectos().size() -1; x >= 0; x--){
                        System.out.println("Jefe del proyecto: " + e.getProyectos().get(x).getNombre());
                        System.out.println(e);
                    }
                }
        }
    }

    private void mostrarDatosEmpleadosPlantilla() {
        List<Empleado> empleados = empleadoRepository.findAll();
        for(Empleado e: empleados){
            if (e.getDatosProfesionales() != null){
                System.out.println(e);
            }
        }

    }

    private void quitarEmpleadoDeProyecto() {
        try{
        Empleado empleado = empleadoRepository.findEmpleadoByDni(Utilidades.pedirPalabra("Introduce el dni del empleado que quieres quitar proyecto"));
        Proyecto proyecto = proyectoRepository.findProyectoByid(Utilidades.pedirInt("Introduce la ID del proyecto"));
        EmpleadoProyecto empleadoProyecto = empleadoProyectoRepository.findAsignacionEmpleadoProyecto(empleado.getDni(),proyecto.getIdProyecto());

        if (Validaciones.comprobarSiUnEmpleadoEsJefeDeUnProyecto(empleado,proyecto)){
            System.out.println("No se puede quitar al jefe del proyecto");
            System.out.println("Para cambiar al jefe vaya a: Gestionar proyectos > Cambiar el jefe de un proyecto");
        }else {
            empleado.eliminarEmpleadosProyectos(empleadoProyecto);
            proyecto.eliminarEmpleadosProyectos(empleadoProyecto);

            empleadoProyectoRepository.remove(empleadoProyecto);
            empleadoRepository.update(empleado);
            proyectoRepository.update(proyecto);
        }

        }catch (NullPointerException nre){
            System.out.println("ERROR, pueden ser las siguientes razones:");
            System.out.println("-El empleado no existe");
            System.out.println("-El proyecto no existe");
            System.out.println("-El empleado no esta asignado a ese proyecto");
        }

    }

    private void asignarEmpleadoAProyecto(){
        Empleado empleado = null;
        Proyecto proyecto = null;
        try {
            empleado = empleadoRepository.findEmpleadoByDni(Utilidades.pedirPalabra("Introduce el dni del empleado que quieres agregar al proyecto"));


            proyecto = proyectoRepository.findProyectoByid(Utilidades.pedirInt("Introduce la ID del proyecto"));

        LocalDate fechaFin;
        LocalDate fechaInicio = Utilidades.parsearFecha(Utilidades.pedirPalabra("Introduce la fecha de inicio en el siguiente formato: DD-MM-YYYY"));
        do{
            fechaFin = Utilidades.parsearFecha(Utilidades.pedirPalabra("Introduce la fecha de finalizacion en el siguiente formato: DD-MM-YYYY"));
            if (!Validaciones.comprobarFechaFinMayorFechaIni(fechaInicio,fechaFin)){
                System.out.println("La fecha fin debe ser mayor que la fecha de inicio");
            }
        }while(!Validaciones.comprobarFechaFinMayorFechaIni(fechaInicio,fechaFin));

        EmpleadoProyecto empleadoProyecto = new EmpleadoProyecto(empleado,proyecto,fechaInicio,fechaFin);

        //Comprobamos que el empleado no este ya en ese proyecto
        if (Validaciones.comprobarEmpleadoEstaEnProyecto(empleado,empleadoProyecto)){
            System.out.println("El empleado ya esta asignado a ese proyecto");
        }else {
            empleado.addlistEmpleadosProyectos(empleadoProyecto);
            proyecto.asignarEmpleadosProyecto(empleadoProyecto);

            empleadoProyectoRepository.crear(empleadoProyecto);
            empleadoRepository.update(empleado);
            proyectoRepository.update(proyecto);
        }
        }catch (NullPointerException nre){
            System.out.println("El empleado o el proyecto no existen");
        }
    }

    private void modificarEmpleado() {
        try {
            String dni = Utilidades.pedirPalabra("Introduce el DNI del empleado que quieres modificar: ");
            Empleado empleado = empleadoRepository.findEmpleadoByDni(dni);
            String categoria = "";
            String sueldoStr;
            BigDecimal sueldo = null;
            String nombre;
            do {
                nombre = Utilidades.pedirString("Introduce el nuevo nombre del empleado");
                sc = new Scanner(System.in);
                if (!Validaciones.validarNombre(nombre)){
                    System.out.println("-Máximo 35 caracteres");
                    System.out.println("-El nombre debe contener al menos 1 letra");
                    System.out.println("-El nombre no debe contener numeros");
                }
            }while (!Validaciones.validarNombre(nombre));

            //Comprobamos si no es un empleado de plantilla
            if (empleado.getDatosProfesionales() == null){
                String ans = Utilidades.pedirPalabra("¿Quieres añadir al empleado a la plantilla? SI/NO");
                if (ans.equalsIgnoreCase("SI")){
                    do {
                        System.out.println("Categorias: A | B | C | D");
                        categoria= Utilidades.pedirPalabra("Introduce la categoria del empleado");
                        if (!Validaciones.comprobarCategoria(categoria)){
                            System.out.println("La categoria introducida no existe");
                        }
                    }while (!Validaciones.comprobarCategoria(categoria));

                    do {
                        sueldoStr = Utilidades.pedirPalabra("Introduce el sueldo del empleado");
                        if (!Validaciones.comprobarSueldo(String.valueOf(sueldoStr))){
                            System.out.println("Los decimales deben separarse por '.' [20000.4]");
                        }else {
                            sueldo = BigDecimal.valueOf(Double.parseDouble(sueldoStr));
                        }
                    }while (!Validaciones.comprobarSueldo(String.valueOf(sueldoStr)));

                    assert sueldo != null;
                    DatosProfesionales datosProfesionales = new DatosProfesionales(empleado,sueldo,Categorias.valueOf(categoria));

                    empleado.setNombre(nombre);
                    empleado.setDatosProfesionales(datosProfesionales);
                    //Guardamos los datos profesionales
                    datosProfesionalesRepository.crear(datosProfesionales);

                    System.out.println("Se ha creado el empleado en plantilla con dni: " + empleado.getDni());

                } else if (ans.equalsIgnoreCase("NO")) {
                    empleado.setNombre(nombre);
                    empleadoRepository.update(empleado);
                    System.out.println("Se ha modificado al empleado");
                }
            }else {
                DatosProfesionales dP = datosProfesionalesRepository.findByDni(dni);
                do {
                    System.out.println("Categorias: A | B | C | D");
                    categoria= Utilidades.pedirPalabra("Introduce la categoria del empleado");
                    if (!Validaciones.comprobarCategoria(categoria)){
                        System.out.println("La categoria introducida no existe");
                    }
                }while (!Validaciones.comprobarCategoria(categoria));

                do {
                    sueldoStr = Utilidades.pedirPalabra("Introduce el sueldo del empleado");
                    if (!Validaciones.comprobarSueldo(String.valueOf(sueldoStr))){
                        System.out.println("Los decimales deben separarse por '.' [20000.4]");
                    }else {
                        sueldo = BigDecimal.valueOf(Double.parseDouble(sueldoStr));
                    }
                }while (!Validaciones.comprobarSueldo(String.valueOf(sueldoStr)));


                dP.setCategorias(Categorias.valueOf(categoria));
                dP.setEmpleadoPlantilla(empleado);
                dP.setSueldoBruto(sueldo);
                empleado.setNombre(nombre);
                empleado.setDatosProfesionales(new DatosProfesionales(empleado,sueldo,Categorias.valueOf(categoria)));
                //Guardamos los datos profesionales
                empleadoRepository.update(empleado);
                datosProfesionalesRepository.update(dP);

                System.out.println("Se ha actualizado el empleado con dni: " + empleado.getDni());
            }
        }catch (InputMismatchException ime){
            System.out.println("El dato introducido no es correcto");
            System.out.println("Los valores decimales deben ir separados por '.' [20000.4]");
        }catch (NullPointerException nre){
            System.out.println("El dni introducido no existe");
        }catch (DateTimeParseException dtpe){
            System.out.println("El formato de fecha debe ser: [dd-mm-YYYY]");
        }
    }

    private void borrarEmpleado() {
        try {
            String dni = Utilidades.pedirPalabra("Introduce el DNI del empleado que quieres borrar: ");
        //Seleccionamos el empleado
        Empleado empleado = empleadoRepository.findEmpleadoByDni(dni);
        //Cogemos sus datos profesionales
        DatosProfesionales datosProfesionales = empleado.getDatosProfesionales();
        //Buscamos las asignaciones de ese empleado
        List<EmpleadoProyecto> asignacionesEmpleado = empleado.getListaEmpleadosProyectos();
        //Buscamos los proyectos en los que es jefe
        List<Proyecto> proyectos = empleado.getProyectos();
        //TablaAsinaciones
        List<EmpleadoProyecto> asignaciones = empleadoProyectoRepository.findAsignacionesByDni(dni);


        //Eliminamos la relacion del lado de Proyectos
        for(int x = proyectos.size() -1 ; x >= 0 ; x--){
            List<EmpleadoProyecto> listaEmpPro = proyectos.get(x).getListaEmpleadosProyectos();
            //for (x = 0; x < listaEmpPro.size(); x++){
            for (int i = listaEmpPro.size() -1; i >= 0; i--){
                if (listaEmpPro.get(x).getEmpleado().getDni().equalsIgnoreCase(dni)){
                    empleadoProyectoRepository.remove(listaEmpPro.get(x));
                }
            }
        }

        for (int x = asignaciones.size() -1; x >=0 ; x--){
            empleadoProyectoRepository.remove(asignaciones.get(x));
        }


        //Eliminamos la relacion del lado empleado
        for (int x = 0; x < empleado.getListaEmpleadosProyectos().size(); x++){
            if (empleado.getListaEmpleadosProyectos().get(x).getEmpleado().getDni().equalsIgnoreCase(dni)){
                empleado.getListaEmpleadosProyectos().remove(empleado.getListaEmpleadosProyectos().get(x));
            }
        }

        if (!proyectos.isEmpty()){
            System.out.println("El empleado es JEFE de uno o mas proyectos");
            Empleado nuevoJefe;
            do {
                nuevoJefe = empleadoRepository.findEmpleadoByDni(Utilidades.pedirPalabra("Introduce el dni del nuevo jefe para esos proyectos"));
                if (nuevoJefe.getDatosProfesionales() == null){
                    System.out.println("Para ser jefe debe estar en plantilla");
                }
            }while (nuevoJefe.getDatosProfesionales() == null);


            //Eliminamos la relacion     Empleado - jefe - Proyecto
            for(int x = proyectos.size() -1 ; x >= 0 ; x--){
                proyectos.get(x).setJefe(nuevoJefe);
                proyectoRepository.update(proyectos.get(x));
            }
        }

        //Eliminamos la relacion     Empleado - realiza - Proyecto

        for (int x = asignacionesEmpleado.size() -1; x >= 0; x--){
            asignacionesEmpleado.remove(asignacionesEmpleado.get(x));
        }

        if (datosProfesionales != null){
            datosProfesionalesRepository.remove(datosProfesionalesRepository.findByDni(dni));
        }
        empleadoRepository.remove(empleado);
        }catch (NullPointerException nre){
            System.out.println("El DNI no existe");
        }

    }

    private void crearEmpleadoOEmpleadoPlantilla() {
        try {
            Scanner scanner = new Scanner(System.in);
            String categoria = "";
            String sueldoStr;
            BigDecimal sueldo = null;
            Empleado empleado= null;;
            String dni = Utilidades.pedirPalabra("Introduce el dni del empleado");
            if (Validaciones.validarDNI(dni) && !dni.isEmpty()){
                String nombre = Utilidades.pedirString("Introduce el nombre del empleado");
                sc = new Scanner(System.in);
                if (Validaciones.validarNombre(nombre)){
                    empleado = new Empleado(dni,nombre);
                    //Guardamos el empleado en la base de datos
                    empleadoRepository.crear(empleado);

                    String ans = Utilidades.pedirPalabra("¿Es un empleado de plantilla? SI/NO");
                    if (ans.equalsIgnoreCase("si")){
                        do {
                            System.out.println("Categorias: A | B | C | D");
                            categoria= Utilidades.pedirPalabra("Introduce la categoria del empleado");
                            if (!Validaciones.comprobarCategoria(categoria)){
                                System.out.println("ERROR,La categoria introducida no existe!!!");
                            }
                        }while (!Validaciones.comprobarCategoria(categoria));

                        do {
                            sueldoStr = Utilidades.pedirPalabra("Introduce el sueldo del empleado");
                            if (!Validaciones.comprobarSueldo(String.valueOf(sueldoStr))){
                                System.out.println("ERROR, los decimales deben separarse por '.' [20000.4]!!!");
                            }else {
                                sueldo = BigDecimal.valueOf(Double.parseDouble(sueldoStr));
                            }
                        }while (!Validaciones.comprobarSueldo(String.valueOf(sueldoStr)));

                        assert sueldo != null;
                        DatosProfesionales datosProfesionales = new DatosProfesionales(empleado,sueldo, Categorias.valueOf(categoria));
                        empleado.setDatosProfesionales(datosProfesionales);
                        //Guardamos los datos profesionales
                        datosProfesionalesRepository.crear(datosProfesionales);
                        System.out.println("Se ha creado el empleado en plantilla con dni: " + empleado.getDni());
                }else if (ans.equalsIgnoreCase("no")){
                        System.out.println("Se ha creado el empleado con dni " + empleado.getDni());
                    }else System.out.println("La opcion introducida no es correcta SI/NO");
            }else {
                    System.out.println("-Máximo 35 caracteres");
                    System.out.println("-El nombre debe contener al menos 1 letra");
                    System.out.println("-El nombre no debe contener numeros");
                }
            }else {
                System.out.println("- ERROR AL INTRODUCIR LOS DATOS: ");
                System.out.println("- El DNI deben tener el siguiente formato: [12345678A]");
                System.out.println("- El DNI debe existir");
            }

        }catch (ConstraintViolationException | EntityExistsException cve){
            System.out.println("Ese dni ya esta registrado");
        }catch (InputMismatchException ime){
            System.out.println("El dato introducido no es correcto");
            System.out.println("Los valores decimales deben ir separados por '.' [20000.4]");
        }catch (NullPointerException nre){
            System.out.println("El DNI no existe");
        }
    }



}
