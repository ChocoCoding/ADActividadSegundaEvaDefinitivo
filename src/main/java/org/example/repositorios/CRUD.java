package org.example.repositorios;

import java.util.List;

public interface CRUD <T>{

    void crear(T t);
    void remove(T t);
    void update(T t);
    List<T> findAll();


}
