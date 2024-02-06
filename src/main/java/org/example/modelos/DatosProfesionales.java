package org.example.modelos;


import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Types;

@Entity
@Table(name = "datos_profesionales")
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"empleadoPlantilla"})
public class DatosProfesionales implements Serializable {
    @Id
    @OneToOne
    @JoinColumn(name = "dni")
    private Empleado empleadoPlantilla;

    @NonNull
    @Column(name = "sueldo_bruto", columnDefinition = "decimal(8,2)")
    private BigDecimal sueldoBruto;

    @NonNull
    @Column(name = "categoria")
    @Enumerated(EnumType.STRING)
    private Categorias categorias;


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Sueldo bruto: ").append(sueldoBruto).append("\n");
        sb.append("Categoria: ").append(categorias.name()).append("\n");
        return sb.toString();
    }
}
