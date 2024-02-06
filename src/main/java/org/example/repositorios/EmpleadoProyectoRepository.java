package org.example.repositorios;

import jakarta.persistence.Query;
import org.example.modelos.Empleado;
import org.example.modelos.EmpleadoProyecto;
import org.example.modelos.Proyecto;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class EmpleadoProyectoRepository implements CRUD<EmpleadoProyecto>{

    Session session;
    public EmpleadoProyectoRepository(Session session){
    this.session = session;
}

    @Override
    public void crear(EmpleadoProyecto empleadoProyecto) {
        Transaction trx = session.beginTransaction();
        session.persist(empleadoProyecto);
        trx.commit();
    }

    @Override
    public void remove(EmpleadoProyecto empleadoProyecto) {
        Transaction trx = session.beginTransaction();
        session.remove(empleadoProyecto);
        trx.commit();
    }

    @Override
    public void update(EmpleadoProyecto empleadoProyecto) {
        Transaction trx = session.beginTransaction();
        session.update(empleadoProyecto);
        trx.commit();
    }

    @Override
    public List<EmpleadoProyecto> findAll() {
        Transaction trx = session.beginTransaction();
        List<EmpleadoProyecto> empleadoProyectos = this.session.createQuery("SELECT ep FROM EmpleadoProyecto ep").getResultList();
        trx.commit();
        return empleadoProyectos;
    }

    public List<EmpleadoProyecto> findAsignacionesByDni(String dni){
        Transaction trx = session.beginTransaction();
        List<EmpleadoProyecto> empleadoProyecto = (List<EmpleadoProyecto>) session.createQuery("SELECT ep FROM EmpleadoProyecto ep WHERE ep.empleado.dni=: dni").setParameter("dni",dni).getResultList();
        trx.commit();
        return empleadoProyecto;
    }


    public EmpleadoProyecto findAsignacionEmpleadoProyecto(String dni,int id){
        Transaction trx = session.beginTransaction();
        Query query = session.createQuery("SELECT ep FROM EmpleadoProyecto ep WHERE ep.empleado.dni=: dni AND ep.proyecto.id =: id");
        query.setParameter("dni",dni);
        query.setParameter("id",id);
        EmpleadoProyecto empleadoProyecto = (EmpleadoProyecto) query.getSingleResult();
        trx.commit();
        return empleadoProyecto;
    }

    public List<EmpleadoProyecto> findAsignacionesById(int id){
        Transaction trx = session.beginTransaction();
        List<EmpleadoProyecto> empleadoProyecto = (List<EmpleadoProyecto>) session.createQuery("SELECT ep FROM EmpleadoProyecto ep WHERE ep.proyecto.id=: id").setParameter("id",id).getResultList();
        trx.commit();
        return empleadoProyecto;
    }
}
