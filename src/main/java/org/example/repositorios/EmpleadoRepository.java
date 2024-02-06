package org.example.repositorios;

import org.example.modelos.Empleado;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class EmpleadoRepository implements CRUD<Empleado>{
    Session session;

    public EmpleadoRepository(Session session){
        this.session = session;
    }


    @Override
    public void crear(Empleado empleado) {
        Transaction trx = session.beginTransaction();
        session.persist(empleado);
        trx.commit();
    }

    @Override
    public void remove(Empleado empleado) {
        Transaction trx = session.beginTransaction();
        session.remove(empleado);
        trx.commit();
    }

    @Override
    public void update(Empleado empleado) {
        Transaction trx = session.beginTransaction();
        session.update(empleado);
        trx.commit();
    }

    @Override
    public List<Empleado> findAll() {
        Transaction trx = session.beginTransaction();
        List<Empleado> empleados =  session.createQuery("SELECT e FROM Empleado e").getResultList();
        trx.commit();
        return empleados;
    }

    public Empleado findEmpleadoByDni(String dni){
        Transaction trx = session.beginTransaction();
        Empleado empleado = (Empleado) session.createQuery("SELECT e FROM Empleado e WHERE e.dni = :dni").setParameter("dni",dni).uniqueResult();
        trx.commit();
        return empleado;
    }




}
