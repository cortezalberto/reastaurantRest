package org.example.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO para crear una nueva empresa
 * Validaciones de entrada para la API REST
 */
@Data
public class CreateEmpresaRequest {

    @NotBlank(message = "El nombre de la empresa es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    @NotBlank(message = "La razón social es obligatoria")
    @Size(min = 2, max = 150, message = "La razón social debe tener entre 2 y 150 caracteres")
    private String razonSocial;

    @NotNull(message = "El CUIL es obligatorio")
    private Integer cuil;
}