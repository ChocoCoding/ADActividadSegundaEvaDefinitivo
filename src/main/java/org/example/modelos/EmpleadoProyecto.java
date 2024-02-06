package org.example.modelos;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "asig_proyecto")
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"empleado","proyecto"})
@NoArgsConstructor
public class EmpleadoProyecto implements Serializable {

    @ManyToOne
    @Id
    @JoinColumn(name = "dni_emp")
    @NonNull
    private Empleado empleado;

    @ManyToOne
    @Id
    @JoinColumn(name = "id_proyecto")
    @NonNull
    private Proyecto proyecto;

    @Id
    @Column(name = "fecha_inicio")
    @NonNull
    private LocalDate fechaInicio;


    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

}
