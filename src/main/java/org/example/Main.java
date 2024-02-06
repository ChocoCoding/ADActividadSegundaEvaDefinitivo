package org.example;

import com.google.protobuf.StringValue;
import org.example.modelos.Categorias;
import org.example.modelos.DatosProfesionales;
import org.example.modelos.Empleado;
import org.example.repositorios.DatosProfesionalesRepository;
import org.example.repositorios.EmpleadoProyectoRepository;
import org.example.repositorios.EmpleadoRepository;
import org.example.repositorios.ProyectoRepository;
import org.example.utilities.HibernateUtil;
import org.example.view.Menu;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

import java.math.BigDecimal;
import java.util.Scanner;

public class Main {

    static EmpleadoRepository empleadoRepository;
    static DatosProfesionalesRepository datosProfesionalesRepository;
    static EmpleadoProyectoRepository empleadoProyectoRepository;
    static ProyectoRepository proyectoRepository;


    public static void main(String[] args) {
        Session session = HibernateUtil.get().openSession();
        Menu menu = new Menu(session);
        menu.dialog();
        session.close();
}
}