package org.example.repositorios;

import org.example.modelos.DatosProfesionales;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class DatosProfesionalesRepository implements CRUD<DatosProfesionales>{

    private Session session;
    public DatosProfesionalesRepository(Session session){
        this.session = session;
    }

    @Override
    public void crear(DatosProfesionales datosProfesionales) {
        Transaction trx = this.session.beginTransaction();
        this.session.persist(datosProfesionales);
        trx.commit();
    }

    @Override
    public void remove(DatosProfesionales datosProfesionales) {
    Transaction trx = this.session.beginTransaction();
    this.session.remove(datosProfesionales);
    trx.commit();
    }

    @Override
    public void update(DatosProfesionales datosProfesionales) {
        Transaction trx = this.session.beginTransaction();
        this.session.update(datosProfesionales);
        trx.commit();
    }


    @Override
    public List<DatosProfesionales> findAll() {
        Transaction trx = this.session.beginTransaction();
        List<DatosProfesionales> datosProfesionales = this.session.createQuery("SELECT dp FROM DatosProfesionales dp").getResultList();
        trx.commit();
        return datosProfesionales;
    }

    public DatosProfesionales findByDni(String dni){
        Transaction trx = session.beginTransaction();
        DatosProfesionales dp = (DatosProfesionales) session.createQuery("SELECT dp FROM DatosProfesionales dp WHERE dp.empleadoPlantilla.dni =: dni").setParameter("dni",dni).getSingleResult();
        trx.commit();
        return dp;
    }




}
