package org.example.entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "paises")
@SuperBuilder
@Getter
@Setter
@ToString(callSuper = true)
public class Pais extends Base {

    @Override
    public String getInfo() {
        return "País: " + getNombre();
    }
}