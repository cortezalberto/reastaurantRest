package org.example.entidades;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categorias")
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, exclude = {"articulos", "subcategorias", "categoriaPadre"})
public class Categoria extends Base {

    @Column(name = "denominacion", nullable = false)
    private String denominacion;

    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Articulo> articulos = new HashSet<>();

    @OneToMany(mappedBy = "categoriaPadre", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Categoria> subcategorias = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_padre_id")
    private Categoria categoriaPadre;

    public void addArticulo(Articulo articulo) {
        articulos.add(articulo);
        articulo.setCategoria(this);
    }

    public void removeArticulo(Articulo articulo) {
        articulos.remove(articulo);
        articulo.setCategoria(null);
    }

    public void addSubcategoria(Categoria subcategoria) {
        subcategoria.setCategoriaPadre(this);
        subcategorias.add(subcategoria);
    }

    public void removeSubcategoria(Categoria subcategoria) {
        subcategoria.setCategoriaPadre(null);
        subcategorias.remove(subcategoria);
    }

    @Override
    public String getInfo() {
        return "Categoria: " + denominacion + " - " + articulos.size() + " artículos";
    }
}