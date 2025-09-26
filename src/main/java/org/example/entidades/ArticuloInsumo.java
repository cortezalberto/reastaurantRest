package org.example.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "articulo_insumo")
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class ArticuloInsumo extends Articulo {

    @Column(name = "precio_compra", nullable = false)
    private double precioCompra;

    @Column(name = "stock_actual")
    private Integer stockActual;

    @Column(name = "stock_maximo")
    private Integer stockMaximo;

    @Column(name = "es_para_elaborar")
    private Boolean esParaElaborar;

    @Override
    public String getInfo() {
        return "ArticuloInsumo: " + getDenominacion() + " - Stock: " + stockActual;
    }
}