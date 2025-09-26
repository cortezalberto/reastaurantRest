package org.example.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "usuarios")
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class Usuario extends Base {

    @Column(name = "auth0_id", unique = true)
    private String auth0Id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Override
    public String getInfo() {
        return "Usuario: " + username + " - " + auth0Id;
    }
}