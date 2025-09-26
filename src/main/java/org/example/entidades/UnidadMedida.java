package org.example.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "unidad_medida")
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class UnidadMedida extends Base {

    @Column(name = "denominacion", nullable = false)
    private String denominacion;

    @Override
    public String getInfo() {
        return "Unidad de Medida: " + denominacion;
    }
}
