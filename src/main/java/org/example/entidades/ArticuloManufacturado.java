package org.example.entidades;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "articulo_manufacturado")
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, exclude = {"detallesDeArticulo"})
public class ArticuloManufacturado extends Articulo {

    @Column(name = "descripcion", length = 500)
    private String descripcion;

    @Column(name = "tiempo_estimado_minutos")
    private int tiempoEstimadoMinutos;

    @Column(name = "preparacion", length = 1000)
    private String preparacion;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "articulo_manufacturado_id")
    @Builder.Default
    private Set<ArticuloManufacturadoDetalle> detallesDeArticulo = new HashSet<>();

    public void addDetalle(ArticuloManufacturadoDetalle detalle) {
        detallesDeArticulo.add(detalle);
    }

    public void removeDetalle(ArticuloManufacturadoDetalle detalle) {
        detallesDeArticulo.remove(detalle);
    }

    @Override
    public String getInfo() {
        return "ArticuloManufacturado: " + getDenominacion() + " - Tiempo: " + tiempoEstimadoMinutos + "min";
    }
}
