CREATE DATABASE IF NOT EXISTS tienda
    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE tienda;

DROP TABLE IF EXISTS articulos;

CREATE TABLE articulos (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    codigo      VARCHAR(20)   NOT NULL UNIQUE,
    nombre      VARCHAR(100)  NOT NULL,
    descripcion VARCHAR(255),
    precio      DECIMAL(10,2) NOT NULL,
    stock       INT           NOT NULL DEFAULT 0
);

INSERT INTO articulos (codigo, nombre, descripcion, precio, stock) VALUES
    ('ART-001', 'Teclado Mecánico', 'Switch rojo, layout español', 45600.50, 15),
    ('ART-002', 'Mouse Inalámbrico', 'Sensor óptico 1600 DPI', 12800.99, 40);

-- ------------------------------------------------------------
-- Stored Procedure usado por CallableStatement
-- Devuelve (OUT) cantidad de articulos, stock total y valor del inventario
-- ------------------------------------------------------------
DROP PROCEDURE IF EXISTS sp_resumen_inventario;

DELIMITER $$
CREATE PROCEDURE sp_resumen_inventario(
    OUT p_cantidad    INT,
    OUT p_stock_total INT,
    OUT p_valor_total DECIMAL(14,2)
)
BEGIN
    SELECT COUNT(*),
           COALESCE(SUM(stock), 0),
           COALESCE(SUM(precio * stock), 0)
      INTO p_cantidad, p_stock_total, p_valor_total
      FROM articulos;
END$$
DELIMITER ;
