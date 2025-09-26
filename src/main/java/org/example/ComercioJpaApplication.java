package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Aplicación Spring Boot para el Sistema de Gestión de Restaurante
 *
 * Esta aplicación proporciona una API REST completa para gestionar:
 * - Empresas y Sucursales
 * - Productos e Inventario (Artículos Insumo y Manufacturados)
 * - Categorías y Promociones
 * - Clientes y Usuarios
 * - Pedidos y Detalles
 * - Estructura Geográfica
 *
 * Características:
 * - API REST con documentación OpenAPI/Swagger
 * - Base de datos H2 embebida con persistencia
 * - Validación automática de datos
 * - Manejo de errores global
 * - Actuator para monitoring
 *
 * @author Sistema de Gestión TechFood Solutions
 * @version 2.0 (Spring Boot)
 */
@SpringBootApplication
@EnableJpaRepositories
public class ComercioJpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ComercioJpaApplication.class, args);
    }
}