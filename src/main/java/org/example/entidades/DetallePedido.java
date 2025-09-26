package org.example.entidades;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "detalle_pedidos")
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class DetallePedido extends Base {

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "sub_total", nullable = false)
    private Double subTotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "articulo_id", nullable = false)
    private Articulo articulo;

    @Override
    public String getInfo() {
        return "DetallePedido: " + cantidad + " x " + (articulo != null ? articulo.getDenominacion() : "N/A");
    }
}