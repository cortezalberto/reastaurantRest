package org.example.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

/**
 * DTO para la entidad Factura
 * Proporciona información de facturas para la API REST
 */
@Data
@Builder
public class FacturaDto {
    private Long id;
    private String nombre;
    private LocalDate fechaFacturacion;
    private Double totalVenta;
    private String formaPago;
    private boolean eliminado;

    // Campos específicos para MercadoPago
    private Integer mpPaymentId;
    private String mpMerchantOrderId;
    private String mpPreferenceId;
    private String mpPaymentType;

    // Relaciones
    private PedidoDto pedido;
}