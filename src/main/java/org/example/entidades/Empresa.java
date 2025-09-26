package org.example.entidades;

import jakarta.persistence.*;
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

    @Column(name = "razon_social")
    private String razonSocial;

    @Column(name = "cuil")
    private Integer cuil;

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
