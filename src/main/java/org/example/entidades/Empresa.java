package org.example.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "empresa")
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, exclude = {"sucursales"})
public class Empresa extends Base {

    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "razon_social", nullable = false)
    private String razonSocial;

    @NotNull
    @Column(name = "cuil", nullable = false, unique = true)
    private Long cuil;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Sucursal> sucursales = new HashSet<>();

    public void addSucursal(Sucursal sucursal) {
        sucursales.add(sucursal);
        sucursal.setEmpresa(this);
    }

    public void removeSucursal(Sucursal sucursal) {
        sucursales.remove(sucursal);
        sucursal.setEmpresa(null);
    }

    @Override
    public String getInfo() {
        return "Empresa: " + getNombre() + " - " + razonSocial + " - " + sucursales.size() + " sucursales";
    }
}
