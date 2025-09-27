package org.example.entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "paises")
@SuperBuilder
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)   // <- clavg(callSuper = true)
public class Pais extends Base {

    @Override
    public String getInfo() {
        return "País: " + getNombre();
    }
}