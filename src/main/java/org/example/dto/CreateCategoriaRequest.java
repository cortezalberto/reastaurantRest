package org.example.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * DTO de request para crear/actualizar categorías
 */
@Data
public class CreateCategoriaRequest {
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    @NotBlank(message = "La denominación es obligatoria")
    @Size(min = 2, max = 150, message = "La denominación debe tener entre 2 y 150 caracteres")
    private String denominacion;

    @NotNull(message = "La sucursal es obligatoria")
    private Long sucursalId;

    private Long categoriaPadreId;
}