package org.example.entidades;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "provincias")
@SuperBuilder
@Getter
@Setter
@ToString(callSuper = true)

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)   // <- clav
public class Provincia extends Base {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pais_id")
    private Pais pais;

    @Override
    public String getInfo() {
        return "Provincia: " + getNombre() + " - " + (pais != null ? pais.getNombre() : "N/A");
    }
}