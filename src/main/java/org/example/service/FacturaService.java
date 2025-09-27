package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.FacturaDto;
import org.example.entidades.Factura;
import org.example.entidades.FormaPago;
import org.example.repository.FacturaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Servicio para la gestión de facturas
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FacturaService {

    private final FacturaRepository facturaRepository;

    /**
     * Obtener factura por ID
     */
    @Transactional(readOnly = true)
    public FacturaDto obtenerFacturaPorId(Long id) {
        log.info("Consultando factura con ID: {}", id);

        Factura factura = facturaRepository.findById(id)
                .filter(f -> !f.isEliminado())
                .orElseThrow(() -> new IllegalArgumentException("Factura no encontrada con ID: " + id));

        return convertirADto(factura);
    }

    /**
     * Listar todas las facturas activas
     */
    @Transactional(readOnly = true)
    public List<FacturaDto> listarFacturasActivas() {
        log.info("Consultando todas las facturas activas");

        List<Factura> facturas = facturaRepository.findByEliminadoFalse();
        return facturas.stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    /**
     * Buscar facturas por fecha
     */
    @Transactional(readOnly = true)
    public List<FacturaDto> buscarPorFecha(LocalDate fecha) {
        log.info("Buscando facturas por fecha: {}", fecha);

        List<Factura> facturas = facturaRepository.findByFechaFacturacionAndEliminadoFalse(fecha);
        return facturas.stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    /**
     * Buscar facturas por rango de fechas
     */
    @Transactional(readOnly = true)
    public List<FacturaDto> buscarPorRangoFechas(LocalDate fechaDesde, LocalDate fechaHasta) {
        log.info("Buscando facturas por rango de fechas: {} - {}", fechaDesde, fechaHasta);

        List<Factura> facturas = facturaRepository.findByFechaFacturacionBetweenAndEliminadoFalse(fechaDesde, fechaHasta);
        return facturas.stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    /**
     * Buscar facturas por forma de pago
     */
    @Transactional(readOnly = true)
    public List<FacturaDto> buscarPorFormaPago(String formaPago) {
        log.info("Buscando facturas por forma de pago: {}", formaPago);

        FormaPago formaPagoEnum = FormaPago.valueOf(formaPago.toUpperCase());
        List<Factura> facturas = facturaRepository.findByFormaPagoAndEliminadoFalse(formaPagoEnum);
        return facturas.stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    /**
     * Obtener estadísticas de facturación
     */
    @Transactional(readOnly = true)
    public Map<String, Object> obtenerEstadisticas(LocalDate fechaDesde, LocalDate fechaHasta) {
        log.info("Obteniendo estadísticas de facturación: {} - {}", fechaDesde, fechaHasta);

        List<Factura> facturas = facturaRepository.findByFechaFacturacionBetweenAndEliminadoFalse(fechaDesde, fechaHasta);

        double totalFacturado = facturas.stream()
                .mapToDouble(f -> f.getTotalVenta() != null ? f.getTotalVenta() : 0.0)
                .sum();

        long totalFacturas = facturas.size();

        long facturasMercadoPago = facturas.stream()
                .filter(f -> FormaPago.MERCADOPAGO.equals(f.getFormaPago()))
                .count();

        long facturasEfectivo = facturas.stream()
                .filter(f -> FormaPago.EFECTIVO.equals(f.getFormaPago()))
                .count();

        return Map.of(
                "fechaDesde", fechaDesde,
                "fechaHasta", fechaHasta,
                "totalFacturado", totalFacturado,
                "totalFacturas", totalFacturas,
                "facturasMercadoPago", facturasMercadoPago,
                "facturasEfectivo", facturasEfectivo,
                "promedioFactura", totalFacturas > 0 ? totalFacturado / totalFacturas : 0.0
        );
    }

    /**
     * Convertir entidad a DTO
     */
    private FacturaDto convertirADto(Factura factura) {
        return FacturaDto.builder()
                .id(factura.getId())
                .nombre(factura.getNombre())
                .fechaFacturacion(factura.getFechaFacturacion())
                .totalVenta(factura.getTotalVenta())
                .formaPago(factura.getFormaPago() != null ? factura.getFormaPago().name() : null)
                .eliminado(factura.isEliminado())
                .mpPaymentId(factura.getMpPaymentId())
                .mpMerchantOrderId(factura.getMpMerchantOrderId())
                .mpPreferenceId(factura.getMpPreferenceId())
                .mpPaymentType(factura.getMpPaymentType())
                .build();
    }
}