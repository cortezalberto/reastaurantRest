package org.example.entidades;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Table(name = "facturas")
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class Factura extends Base {

    @Column(name = "fecha_facturacion")
    private LocalDate fechaFacturacion;

    @Column(name = "mp_payment_id")
    private Integer mpPaymentId;

    @Column(name = "mp_merchant_order_id")
    private String mpMerchantOrderId;

    @Column(name = "mp_preference_id")
    private String mpPreferenceId;

    @Column(name = "mp_payment_type")
    private String mpPaymentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "forma_pago")
    private FormaPago formaPago;

    @Column(name = "total_venta")
    private Double totalVenta;

    @Override
    public String getInfo() {
        return "Factura: " + getId() + " - $" + totalVenta + " - " + formaPago;
    }
}
