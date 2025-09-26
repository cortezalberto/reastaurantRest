package org.example.entidades;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "articulo_manufacturado_detalle")
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class ArticuloManufacturadoDetalle extends Base {

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "articulo_insumo_id", nullable = false)
    private ArticuloInsumo articuloInsumo;

    @Override
    public String getInfo() {
        return "Detalle: " + cantidad + " x " + (articuloInsumo != null ? articuloInsumo.getDenominacion() : "N/A");
    }
}
