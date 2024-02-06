package org.example.repositorios;

import org.example.modelos.Empleado;
import org.example.modelos.Proyecto;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ProyectoRepository implements CRUD<Proyecto>{

    Session session;
    public ProyectoRepository(Session session){
        this.session = session;
    }


    @Override
    public void crear(Proyecto proyecto) {
        Transaction trx = session.beginTransaction();
        session.persist(proyecto);
        trx.commit();
    }

    @Override
    public void remove(Proyecto proyecto) {
        Transaction trx = session.beginTransaction();
        session.remove(proyecto);
        trx.commit();
    }

    @Override
    public void update(Proyecto proyecto) {
        Transaction trx = session.beginTransaction();
        session.update(proyecto);
        trx.commit();
    }

    @Override
    public List<Proyecto> findAll() {
        Transaction trx = session.beginTransaction();
        List<Proyecto> proyectos = this.session.createQuery("SELECT p FROM Proyecto p").getResultList();
        trx.commit();
        return proyectos;
    }

    public Proyecto findProyectoByid(int id){
        Transaction trx = session.beginTransaction();
        Proyecto proyecto = (Proyecto) this.session.createQuery("SELECT p FROM Proyecto p WHERE id =: id").setParameter("id",id).uniqueResult();
        trx.commit();
        return proyecto;
    }

}
