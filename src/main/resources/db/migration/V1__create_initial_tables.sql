-- V1__create_initial_tables.sql
-- Migración inicial: creación de todas las tablas del sistema

-- Tabla de usuarios
CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL,
    `email` VARCHAR(255) NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    `phone` VARCHAR(255) NOT NULL,
    `rol` VARCHAR(20) NOT NULL DEFAULT 'CLIENTE',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla de barberías
CREATE TABLE IF NOT EXISTS `barberia` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `nombre` VARCHAR(255) NOT NULL,
    `direccion` VARCHAR(255) NOT NULL,
    `telefono` VARCHAR(255),
    `email` VARCHAR(255),
    `imagen_url` VARCHAR(500),
    `latitud` DOUBLE NOT NULL,
    `longitud` DOUBLE NOT NULL,
    `activa` BOOLEAN NOT NULL DEFAULT TRUE,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla de servicios
CREATE TABLE IF NOT EXISTS `servicio` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `nombre` VARCHAR(255) NOT NULL,
    `descripcion` VARCHAR(500),
    `precio` DOUBLE NOT NULL,
    `duracion_minutos` INT NOT NULL,
    `activo` BOOLEAN NOT NULL DEFAULT TRUE,
    `barberia_id` BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_servicio_barberia` FOREIGN KEY (`barberia_id`) REFERENCES `barberia` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla de barberos (relación usuario-barbería)
CREATE TABLE IF NOT EXISTS `barbero` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `usuario_id` BIGINT NOT NULL,
    `barberia_id` BIGINT NOT NULL,
    `activo` BOOLEAN NOT NULL DEFAULT TRUE,
    `especialidad` VARCHAR(255),
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_barbero_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `user` (`id`),
    CONSTRAINT `fk_barbero_barberia` FOREIGN KEY (`barberia_id`) REFERENCES `barberia` (`id`),
    UNIQUE KEY `uk_barbero_usuario_barberia` (`usuario_id`, `barberia_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla de horarios de barberos
CREATE TABLE IF NOT EXISTS `barbero_horarios` (
    `barbero_id` BIGINT NOT NULL,
    `horario` VARCHAR(255),
    CONSTRAINT `fk_horarios_barbero` FOREIGN KEY (`barbero_id`) REFERENCES `barbero` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla de relación barbero-servicios
CREATE TABLE IF NOT EXISTS `barbero_servicios` (
    `barbero_id` BIGINT NOT NULL,
    `servicio_id` BIGINT NOT NULL,
    PRIMARY KEY (`barbero_id`, `servicio_id`),
    CONSTRAINT `fk_barbero_servicios_barbero` FOREIGN KEY (`barbero_id`) REFERENCES `barbero` (`id`),
    CONSTRAINT `fk_barbero_servicios_servicio` FOREIGN KEY (`servicio_id`) REFERENCES `servicio` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla de citas
CREATE TABLE IF NOT EXISTS `cita` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `cliente_id` BIGINT NOT NULL,
    `barbero_id` BIGINT NOT NULL,
    `servicio_id` BIGINT NOT NULL,
    `fecha_hora` DATETIME NOT NULL,
    `estado` VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE',
    `observaciones` VARCHAR(500),
    `precio` DOUBLE NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_cita_cliente` FOREIGN KEY (`cliente_id`) REFERENCES `user` (`id`),
    CONSTRAINT `fk_cita_barbero` FOREIGN KEY (`barbero_id`) REFERENCES `barbero` (`id`),
    CONSTRAINT `fk_cita_servicio` FOREIGN KEY (`servicio_id`) REFERENCES `servicio` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla de auditoría de Flyway
CREATE TABLE IF NOT EXISTS `flyway_schema_history` (
    `installed_rank` INT NOT NULL,
    `version` VARCHAR(50),
    `description` VARCHAR(200) NOT NULL,
    `type` VARCHAR(20) NOT NULL,
    `script` VARCHAR(1000) NOT NULL,
    `checksum` INT,
    `installed_by` VARCHAR(100) NOT NULL,
    `installed_on` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `execution_time` INT NOT NULL,
    `success` BOOLEAN NOT NULL,
    PRIMARY KEY (`installed_rank`),
    KEY `flyway_schema_history_s_idx` (`success`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
