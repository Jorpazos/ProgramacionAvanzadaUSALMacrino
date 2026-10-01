-- =====================================================================
-- Script DDL - Parcial Programacion Avanzada (2do Cuatrimestre 2025)
-- Base de datos: MySQL 8.x (probado tambien en MariaDB 10.11)
-- Ejecutar con:  mysql -u root -p < 01_ddl_logistica.sql
-- Incluye: tablas, restricciones, tabla de distancias y stored procedures
-- (los procedures se usan desde Java con CallableStatement).
-- =====================================================================

DROP DATABASE IF EXISTS logistica;
CREATE DATABASE logistica CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE logistica;

-- ---------------------------------------------------------------------
-- Categoria de licencia del chofer: define las toneladas que puede llevar
-- ---------------------------------------------------------------------
CREATE TABLE categoria (
    codigo        CHAR(1)  NOT NULL,
    toneladas_max INT      NOT NULL,
    CONSTRAINT pk_categoria PRIMARY KEY (codigo),
    CONSTRAINT ck_categoria_ton CHECK (toneladas_max > 0)
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- Destinos permitidos (la empresa solo transporta a estas 8 ciudades)
-- ---------------------------------------------------------------------
CREATE TABLE destino (
    codigo VARCHAR(20) NOT NULL,
    nombre VARCHAR(40) NOT NULL,
    CONSTRAINT pk_destino PRIMARY KEY (codigo)
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- Tabla de distancias en km (se carga en ambos sentidos: A->B y B->A)
-- ---------------------------------------------------------------------
CREATE TABLE distancia (
    origen  VARCHAR(20) NOT NULL,
    destino VARCHAR(20) NOT NULL,
    km      INT         NOT NULL,
    CONSTRAINT pk_distancia PRIMARY KEY (origen, destino),
    CONSTRAINT fk_distancia_origen  FOREIGN KEY (origen)  REFERENCES destino (codigo),
    CONSTRAINT fk_distancia_destino FOREIGN KEY (destino) REFERENCES destino (codigo),
    CONSTRAINT ck_distancia_km   CHECK (km > 0),
    CONSTRAINT ck_distancia_dist CHECK (origen <> destino)
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- Camiones
-- ---------------------------------------------------------------------
CREATE TABLE camion (
    id                     BIGINT        NOT NULL AUTO_INCREMENT,
    marca                  VARCHAR(40)   NOT NULL,
    modelo                 VARCHAR(40)   NOT NULL,
    dominio                VARCHAR(10)   NOT NULL,
    toneladas_max          DECIMAL(6,2)  NOT NULL,
    capacidad_tanque_litros DECIMAL(8,2) NOT NULL,
    consumo_litros_km      DECIMAL(6,3)  NOT NULL,
    CONSTRAINT pk_camion PRIMARY KEY (id),
    CONSTRAINT uq_camion_dominio UNIQUE (dominio),
    CONSTRAINT ck_camion_ton      CHECK (toneladas_max > 0),
    CONSTRAINT ck_camion_tanque   CHECK (capacidad_tanque_litros > 0),
    CONSTRAINT ck_camion_consumo  CHECK (consumo_litros_km > 0)
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- Choferes (datos personales + categoria + telefono celular)
-- ---------------------------------------------------------------------
CREATE TABLE chofer (
    id               BIGINT       NOT NULL AUTO_INCREMENT,
    nombre           VARCHAR(50)  NOT NULL,
    apellido         VARCHAR(50)  NOT NULL,
    dni              VARCHAR(8)   NOT NULL,
    fecha_nacimiento DATE         NOT NULL,
    categoria        CHAR(1)      NOT NULL,
    telefono         VARCHAR(15)  NOT NULL,
    CONSTRAINT pk_chofer PRIMARY KEY (id),
    CONSTRAINT uq_chofer_dni UNIQUE (dni),
    CONSTRAINT fk_chofer_categoria FOREIGN KEY (categoria) REFERENCES categoria (codigo)
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- Relacion N a N: un chofer puede manejar varios camiones y un camion
-- puede ser manejado por varios choferes.
-- ---------------------------------------------------------------------
CREATE TABLE chofer_camion (
    chofer_id BIGINT NOT NULL,
    camion_id BIGINT NOT NULL,
    CONSTRAINT pk_chofer_camion PRIMARY KEY (chofer_id, camion_id),
    CONSTRAINT fk_cc_chofer FOREIGN KEY (chofer_id) REFERENCES chofer (id) ON DELETE CASCADE,
    CONSTRAINT fk_cc_camion FOREIGN KEY (camion_id) REFERENCES camion (id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- Usuarios del sistema. Perfil ADMIN (sin chofer) o CHOFER (ligado a un chofer)
-- ---------------------------------------------------------------------
CREATE TABLE usuario (
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    username      VARCHAR(30)  NOT NULL,
    password_hash VARCHAR(200) NOT NULL,
    rol           VARCHAR(10)  NOT NULL,
    chofer_id     BIGINT       NULL,
    CONSTRAINT pk_usuario PRIMARY KEY (id),
    CONSTRAINT uq_usuario_username UNIQUE (username),
    CONSTRAINT uq_usuario_chofer UNIQUE (chofer_id),
    CONSTRAINT fk_usuario_chofer FOREIGN KEY (chofer_id) REFERENCES chofer (id) ON DELETE CASCADE,
    CONSTRAINT ck_usuario_rol CHECK (
        (rol = 'ADMIN' AND chofer_id IS NULL) OR (rol = 'CHOFER' AND chofer_id IS NOT NULL))
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- Tokens de la cookie "recordarme" (sesion recordada)
-- ---------------------------------------------------------------------
CREATE TABLE sesion_recordada (
    token      CHAR(64) NOT NULL,
    usuario_id BIGINT   NOT NULL,
    expira     DATETIME NOT NULL,
    CONSTRAINT pk_sesion_recordada PRIMARY KEY (token),
    CONSTRAINT fk_sesion_usuario FOREIGN KEY (usuario_id) REFERENCES usuario (id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- Viajes. Estados: ASIGNADO -> EN_CURSO -> FINALIZADO
-- ---------------------------------------------------------------------
CREATE TABLE viaje (
    id               BIGINT       NOT NULL AUTO_INCREMENT,
    chofer_id        BIGINT       NOT NULL,
    camion_id        BIGINT       NOT NULL,
    origen           VARCHAR(20)  NOT NULL,
    destino          VARCHAR(20)  NOT NULL,
    distancia_km     INT          NOT NULL,
    dias_estimados   INT          NOT NULL,
    litros_estimados DECIMAL(10,2) NOT NULL,
    tanques          INT          NOT NULL,
    estado           VARCHAR(12)  NOT NULL DEFAULT 'ASIGNADO',
    fecha_carga      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_inicio     DATETIME     NULL,
    fecha_fin        DATETIME     NULL,
    CONSTRAINT pk_viaje PRIMARY KEY (id),
    CONSTRAINT fk_viaje_chofer  FOREIGN KEY (chofer_id) REFERENCES chofer (id),
    CONSTRAINT fk_viaje_camion  FOREIGN KEY (camion_id) REFERENCES camion (id),
    CONSTRAINT fk_viaje_origen  FOREIGN KEY (origen)  REFERENCES destino (codigo),
    CONSTRAINT fk_viaje_destino FOREIGN KEY (destino) REFERENCES destino (codigo),
    CONSTRAINT ck_viaje_estado  CHECK (estado IN ('ASIGNADO', 'EN_CURSO', 'FINALIZADO')),
    CONSTRAINT ck_viaje_od      CHECK (origen <> destino),
    CONSTRAINT ck_viaje_km      CHECK (distancia_km > 0)
) ENGINE=InnoDB;

CREATE INDEX ix_viaje_chofer ON viaje (chofer_id, estado);
CREATE INDEX ix_viaje_camion ON viaje (camion_id, estado);

-- ---------------------------------------------------------------------
-- Datos maestros
-- ---------------------------------------------------------------------
INSERT INTO categoria (codigo, toneladas_max) VALUES
    ('A', 10), ('B', 20), ('C', 30), ('D', 40);

INSERT INTO destino (codigo, nombre) VALUES
    ('CABA', 'CABA'),
    ('CORDOBA', 'Córdoba'),
    ('CORRIENTES', 'Corrientes'),
    ('FORMOSA', 'Formosa'),
    ('LA_PLATA', 'La Plata'),
    ('LA_RIOJA', 'La Rioja'),
    ('MENDOZA', 'Mendoza'),
    ('NEUQUEN', 'Neuquén');

-- Tabla de distancias del enunciado (km)
INSERT INTO distancia (origen, destino, km) VALUES
    ('CABA', 'CORDOBA', 646),
    ('CABA', 'CORRIENTES', 792),
    ('CABA', 'FORMOSA', 933),
    ('CABA', 'LA_PLATA', 53),
    ('CABA', 'LA_RIOJA', 986),
    ('CABA', 'MENDOZA', 985),
    ('CABA', 'NEUQUEN', 989),
    ('CORDOBA', 'CABA', 646),
    ('CORDOBA', 'CORRIENTES', 677),
    ('CORDOBA', 'FORMOSA', 824),
    ('CORDOBA', 'LA_PLATA', 698),
    ('CORDOBA', 'LA_RIOJA', 340),
    ('CORDOBA', 'MENDOZA', 466),
    ('CORDOBA', 'NEUQUEN', 907),
    ('CORRIENTES', 'CABA', 792),
    ('CORRIENTES', 'CORDOBA', 677),
    ('CORRIENTES', 'FORMOSA', 157),
    ('CORRIENTES', 'LA_PLATA', 830),
    ('CORRIENTES', 'LA_RIOJA', 814),
    ('CORRIENTES', 'MENDOZA', 1131),
    ('CORRIENTES', 'NEUQUEN', 1534),
    ('FORMOSA', 'CABA', 933),
    ('FORMOSA', 'CORDOBA', 824),
    ('FORMOSA', 'CORRIENTES', 157),
    ('FORMOSA', 'LA_PLATA', 968),
    ('FORMOSA', 'LA_RIOJA', 927),
    ('FORMOSA', 'MENDOZA', 1269),
    ('FORMOSA', 'NEUQUEN', 1690),
    ('LA_PLATA', 'CABA', 53),
    ('LA_PLATA', 'CORDOBA', 698),
    ('LA_PLATA', 'CORRIENTES', 830),
    ('LA_PLATA', 'FORMOSA', 968),
    ('LA_PLATA', 'LA_RIOJA', 1038),
    ('LA_PLATA', 'MENDOZA', 1029),
    ('LA_PLATA', 'NEUQUEN', 1005),
    ('LA_RIOJA', 'CABA', 986),
    ('LA_RIOJA', 'CORDOBA', 340),
    ('LA_RIOJA', 'CORRIENTES', 814),
    ('LA_RIOJA', 'FORMOSA', 927),
    ('LA_RIOJA', 'LA_PLATA', 1038),
    ('LA_RIOJA', 'MENDOZA', 427),
    ('LA_RIOJA', 'NEUQUEN', 1063),
    ('MENDOZA', 'CABA', 985),
    ('MENDOZA', 'CORDOBA', 466),
    ('MENDOZA', 'CORRIENTES', 1131),
    ('MENDOZA', 'FORMOSA', 1269),
    ('MENDOZA', 'LA_PLATA', 1029),
    ('MENDOZA', 'LA_RIOJA', 427),
    ('MENDOZA', 'NEUQUEN', 676),
    ('NEUQUEN', 'CABA', 989),
    ('NEUQUEN', 'CORDOBA', 907),
    ('NEUQUEN', 'CORRIENTES', 1534),
    ('NEUQUEN', 'FORMOSA', 1690),
    ('NEUQUEN', 'LA_PLATA', 1005),
    ('NEUQUEN', 'LA_RIOJA', 1063),
    ('NEUQUEN', 'MENDOZA', 676);

-- ---------------------------------------------------------------------
-- Stored procedures (se invocan con CallableStatement)
-- ---------------------------------------------------------------------
DELIMITER $$

-- Camiones que el chofer tiene autorizados, que entran en su categoria
-- y que NO estan en un viaje pendiente (ASIGNADO) ni en curso.
CREATE PROCEDURE sp_camiones_disponibles (IN p_chofer_id BIGINT)
BEGIN
    SELECT c.id, c.marca, c.modelo, c.dominio, c.toneladas_max,
           c.capacidad_tanque_litros, c.consumo_litros_km
      FROM camion c
      JOIN chofer_camion cc ON cc.camion_id = c.id AND cc.chofer_id = p_chofer_id
      JOIN chofer ch        ON ch.id = cc.chofer_id
      JOIN categoria cat    ON cat.codigo = ch.categoria
     WHERE c.toneladas_max <= cat.toneladas_max
       AND NOT EXISTS (SELECT 1 FROM viaje v
                        WHERE v.camion_id = c.id
                          AND v.estado IN ('ASIGNADO', 'EN_CURSO'))
     ORDER BY c.marca, c.modelo, c.dominio;
END$$

-- Marca un viaje como FINALIZADO. Solo si pertenece al chofer y esta EN_CURSO.
-- Devuelve en p_filas la cantidad de filas modificadas (0 = no se pudo).
CREATE PROCEDURE sp_finalizar_viaje (IN p_viaje_id BIGINT, IN p_chofer_id BIGINT, OUT p_filas INT)
BEGIN
    UPDATE viaje
       SET estado = 'FINALIZADO', fecha_fin = NOW()
     WHERE id = p_viaje_id AND chofer_id = p_chofer_id AND estado = 'EN_CURSO';
    SET p_filas = ROW_COUNT();
END$$

DELIMITER ;
