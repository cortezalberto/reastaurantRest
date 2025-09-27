package org.example.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * DTO para la entidad Pedido
 * Proporciona información de pedidos para la API REST
 */
@Data
@Builder
public class PedidoDto {
    private Long id;
    private String nombre;
    private LocalDate fechaPedido;
    private LocalTime horaEstimadaFinalizacion;
    private Double total;
    private Double totalCosto;
    private String estado;
    private String formaPago;
    private String tipoDeEnvio;
    private boolean eliminado;
    private ClienteDto cliente;
    private String sucursal;
    private DomicilioDto domicilio;
    private List<DetallePedidoDto> detalles;
    private Integer cantidadItems;
    private FacturaDto factura;

    /**
     * DTO para DetallePedido
     */
    @Data
    @Builder
    public static class DetallePedidoDto {
        private Long id;
        private String nombre;
        private Integer cantidad;
        private Double subTotal;
        private String articulo;
    }

    /**
     * DTO para Domicilio
     */
    @Data
    @Builder
    public static class DomicilioDto {
        private Long id;
        private String nombre;
        private Integer numero;
        private Integer cp;
        private String localidad;
        private String provincia;
        private String pais;
    }
}