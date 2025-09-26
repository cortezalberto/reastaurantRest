package org.example.entidades;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "promociones")
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, exclude = {"imagenes", "articulos"})
public class Promocion extends Base {

    @Column(name = "denominacion", nullable = false)
    private String denominacion;

    @Column(name = "fecha_desde")
    private LocalDate fechaDesde;

    @Column(name = "fecha_hasta")
    private LocalDate fechaHasta;

    @Column(name = "hora_desde")
    private LocalTime horaDesde;

    @Column(name = "hora_hasta")
    private LocalTime horaHasta;

    @Column(name = "precio_descuento")
    private double precioDescuento;

    @Column(name = "precio_promocional")
    private double precioPromocional;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_promocion")
    private TipoPromocion tipoPromocion;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "promocion_id")
    @Builder.Default
    private Set<Imagen> imagenes = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "promocion_articulo",
            joinColumns = @JoinColumn(name = "promocion_id"),
            inverseJoinColumns = @JoinColumn(name = "articulo_id"))
    @Builder.Default
    private Set<Articulo> articulos = new HashSet<>();

    public void addImagen(Imagen imagen) {
        imagenes.add(imagen);
    }

    public void removeImagen(Imagen imagen) {
        imagenes.remove(imagen);
    }

    public void addArticulo(Articulo articulo) {
        articulos.add(articulo);
    }

    public void removeArticulo(Articulo articulo) {
        articulos.remove(articulo);
    }

    @Override
    public String getInfo() {
        return "Promoción: " + denominacion + " - " + tipoPromocion + " - $" + precioPromocional;
    }
}