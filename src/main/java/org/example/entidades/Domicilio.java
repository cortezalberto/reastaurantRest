package org.example.entidades;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "domicilios")
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class Domicilio extends Base {

    @Column(name = "numero", nullable = false)
    private int numero;

    @Column(name = "codigo_postal", nullable = false)
    private int cp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "localidad_id")
    private Localidad localidad;

    @Override
    public String getInfo() {
        return "Domicilio: " + getNombre() + " " + numero + " - CP: " + cp;
    }
}