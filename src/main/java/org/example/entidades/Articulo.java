package org.example.entidades;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "articulo")
@Inheritance(strategy = InheritanceType.JOINED)
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, exclude = {"imagenes", "categoria"})
public abstract class Articulo extends Base {

    @Column(name = "denominacion", nullable = false)
    private String denominacion;

    @Column(name = "precio_venta", nullable = false)
    private double precioVenta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unidad_medida_id")
    private UnidadMedida unidadMedida;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "articulo_id")
    @Builder.Default
    private Set<Imagen> imagenes = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    public void addImagen(Imagen imagen) {
        imagenes.add(imagen);
    }

    public void removeImagen(Imagen imagen) {
        imagenes.remove(imagen);
    }
}