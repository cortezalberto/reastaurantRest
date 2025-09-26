package org.example.entidades;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "pedidos")
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, exclude = {"detallePedidos", "sucursal", "domicilio", "factura", "cliente"})
public class Pedido extends Base {

    @Column(name = "hora_estimada_finalizacion")
    private LocalTime horaEstimadaFinalizacion;

    @Column(name = "total")
    private Double total;

    @Column(name = "total_costo")
    private Double totalCosto;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private Estado estado;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_de_envio")
    private TipoDeEnvio tipoDeEnvio;

    @Enumerated(EnumType.STRING)
    @Column(name = "forma_pago")
    private FormaPago formaPago;

    @Column(name = "fecha_pedido")
    private LocalDate fechaPedido;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id")
    @Builder.Default
    private Set<DetallePedido> detallePedidos = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sucursal_id")
    private Sucursal sucursal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "domicilio_id")
    private Domicilio domicilio;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "factura_id")
    private Factura factura;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    public void addDetallePedido(DetallePedido detallePedido) {
        detallePedidos.add(detallePedido);
    }

    public void removeDetallePedido(DetallePedido detallePedido) {
        detallePedidos.remove(detallePedido);
    }

    @Override
    public String getInfo() {
        return "Pedido: " + getId() + " - " + estado + " - $" + total;
    }
}