CREATE DATABASE IF NOT EXISTS biblioteca;
USE biblioteca;

DROP TABLE IF EXISTS prestamos;
DROP TABLE IF EXISTS libros;

CREATE TABLE libros (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(150) NOT NULL,
    autor VARCHAR(100) NOT NULL,
    stock INT NOT NULL
);

CREATE TABLE prestamos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    libro_id INT,
    usuario VARCHAR(100),
    fecha_prestamo DATE,
    FOREIGN KEY (libro_id) REFERENCES libros(id)
);

-- Carga inicial de datos
INSERT INTO libros (titulo, autor, stock) VALUES
('Clean Code', 'Robert C. Martin', 5),
('Design Patterns', 'Erich Gamma', 2),
('Java: The Complete Reference', 'Herbert Schildt', 0),
('Effective Java', 'Joshua Bloch', 3);

-- Procedimiento almacenado para realizar un préstamo
DELIMITER //
CREATE PROCEDURE sp_realizar_prestamo(
    IN p_libro_id INT,
    IN p_usuario VARCHAR(100),
    OUT p_codigo_respuesta VARCHAR(150)
)
BEGIN
    DECLARE v_stock INT;
    
    SELECT stock INTO v_stock FROM libros WHERE id = p_libro_id;
    
    IF v_stock IS NULL THEN
        SET p_codigo_respuesta = 'Libro no existe';
    ELSEIF v_stock > 0 THEN
        -- Restar stock
        UPDATE libros SET stock = stock - 1 WHERE id = p_libro_id;
        -- Registrar préstamo
        INSERT INTO prestamos (libro_id, usuario, fecha_prestamo) VALUES (p_libro_id, p_usuario, CURDATE());
        SET p_codigo_respuesta = 'Operacion realizada con exito'; 
    ELSE
        SET p_codigo_respuesta = 'Sin stock'; 
    END IF;
END //
DELIMITER ;