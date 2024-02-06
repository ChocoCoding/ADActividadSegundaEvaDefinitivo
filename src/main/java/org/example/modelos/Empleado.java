package org.example.modelos;


import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "empleado")
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor(force = true)
@EqualsAndHashCode(exclude = {"listaEmpleadosProyectos", "datosProfesionales","proyectos"})
public class Empleado implements Serializable {
    @Id
    @Column(name = "dni",columnDefinition = "char(9)")
    @NonNull
    private String dni;
    @NonNull
    @Column(name = "nombre")
    private String nombre;

    @OneToMany(mappedBy = "empleado")
    private List<EmpleadoProyecto> listaEmpleadosProyectos = new ArrayList<>();

    @OneToOne(mappedBy = "empleadoPlantilla")
    private DatosProfesionales datosProfesionales;

    @OneToMany(mappedBy = "jefe")
    private List<Proyecto> proyectos= new ArrayList<>();

    public void addProyecto(Proyecto proyecto){
        proyectos.add(proyecto);
    }

    public void eliminarProyectos(Proyecto proyecto){
        this.proyectos.remove(proyecto);
    }

    public void addlistEmpleadosProyectos(EmpleadoProyecto empleadoProyecto){
        this.listaEmpleadosProyectos.add(empleadoProyecto);
    }

    public void eliminarEmpleadosProyectos(EmpleadoProyecto empleadoProyecto){
        this.listaEmpleadosProyectos.remove(empleadoProyecto);
    }



    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Dni: ").append(dni).append('\n');
        sb.append("Nombre: ").append(nombre).append('\n');
        sb.append("DatosProfesionales: ").append('\n');;
        sb.append("Categoria: ").append(this.datosProfesionales.getCategorias().name()).append('\n');;
        sb.append("Sueldo bruto: ").append(this.datosProfesionales.getSueldoBruto()).append('\n');;
        return sb.toString();
    }
}
