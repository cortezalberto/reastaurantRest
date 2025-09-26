package org.example.entidades;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "sucursal")
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, exclude = {"promociones", "categorias", "empresa"})
public class Sucursal extends Base {

    @Column(name = "horario_apertura")
    private LocalTime horarioApertura;

    @Column(name = "horario_cierre")
    private LocalTime horarioCierre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "sucursal_id")
    @Builder.Default
    private Set<Promocion> promociones = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "sucursal_id")
    @Builder.Default
    private Set<Categoria> categorias = new HashSet<>();

    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "domicilio_id")
    private Domicilio domicilio;

    public void addCategoria(Categoria categoria) {
        categorias.add(categoria);
    }

    public void removeCategoria(Categoria categoria) {
        categorias.remove(categoria);
    }

    public void addPromocion(Promocion promocion) {
        promociones.add(promocion);
    }

    public void removePromocion(Promocion promocion) {
        promociones.remove(promocion);
    }

    @Override
    public String getInfo() {
        return "Sucursal: " + getNombre() + " - " + categorias.size() + " categorías";
    }
}