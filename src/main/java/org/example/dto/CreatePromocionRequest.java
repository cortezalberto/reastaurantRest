package org.example.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * DTO de request para crear/actualizar promociones
 */
@Data
public class CreatePromocionRequest {
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    @NotBlank(message = "La denominación es obligatoria")
    @Size(min = 2, max = 150, message = "La denominación debe tener entre 2 y 150 caracteres")
    private String denominacion;

    @NotNull(message = "La fecha desde es obligatoria")
    @FutureOrPresent(message = "La fecha desde debe ser hoy o en el futuro")
    private LocalDate fechaDesde;

    @NotNull(message = "La fecha hasta es obligatoria")
    @Future(message = "La fecha hasta debe ser en el futuro")
    private LocalDate fechaHasta;

    @NotNull(message = "La hora desde es obligatoria")
    private LocalTime horaDesde;

    @NotNull(message = "La hora hasta es obligatoria")
    private LocalTime horaHasta;

    @DecimalMin(value = "0.0", message = "El precio de descuento no puede ser negativo")
    private Double precioDescuento;

    @NotNull(message = "El precio promocional es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio promocional debe ser mayor a 0")
    private Double precioPromocional;

    @NotBlank(message = "El tipo de promoción es obligatorio")
    @Pattern(regexp = "HAPPYHOUR|PROMOCION1", message = "El tipo debe ser HAPPYHOUR o PROMOCION1")
    private String tipoPromocion;

    @NotNull(message = "La sucursal es obligatoria")
    private Long sucursalId;

    private List<Long> articuloIds;
}