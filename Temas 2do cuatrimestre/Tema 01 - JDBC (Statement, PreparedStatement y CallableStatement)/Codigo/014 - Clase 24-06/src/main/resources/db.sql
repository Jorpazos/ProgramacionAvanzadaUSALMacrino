CREATE DATABASE IF NOT EXISTS universidad;
USE universidad;

DROP TABLE IF EXISTS estudiantes;

CREATE TABLE estudiantes (
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             nombre VARCHAR(100) NOT NULL,
                             edad INT NOT NULL,
                             carrera VARCHAR(100) NOT NULL,
                             promedio DOUBLE NOT NULL
);

INSERT INTO estudiantes (nombre, edad, carrera, promedio) VALUES
                                                              ('Lucas Juárez', 21, 'Sistemas', 8.5), ('Anabella Rossi', 23, 'Sistemas', 7.2),
                                                              ('Mateo Fernández', 20, 'Industrial', 6.8), ('Sofía Benítez', 22, 'Mecánica', 9.1),
                                                              ('Diego Gómez', 25, 'Sistemas', 7.9), ('Valentina Luna', 19, 'Química', 8.3),
                                                              ('Bautista Sosa', 24, 'Industrial', 5.4), ('Camila Díaz', 21, 'Sistemas', 9.5),
                                                              ('Thiago Alvarez', 22, 'Mecánica', 6.2), ('Martina Pérez', 20, 'Química', 7.8),
                                                              ('Bruno Torres', 23, 'Sistemas', 8.1), ('Laia Morales', 21, 'Industrial', 8.9),
                                                              ('Joaquín Ríos', 26, 'Mecánica', 6.5), ('Victoria Castro', 20, 'Sistemas', 7.3),
                                                              ('Tomás Ortiz', 22, 'Química', 7.0), ('Mía Silva', 19, 'Industrial', 8.7),
                                                              ('Ian Acosta', 24, 'Sistemas', 5.9), ('Elena Mendoza', 23, 'Mecánica', 9.2),
                                                              ('Agustín Romero', 21, 'Química', 6.4), ('Clara Novoa', 22, 'Sistemas', 8.0),
                                                              ('Facundo Vera', 25, 'Industrial', 7.1), ('Zoe Giménez', 20, 'Mecánica', 8.4),
                                                              ('Leonel Duarte', 21, 'Sistemas', 6.9), ('Emma Burgos', 19, 'Química', 9.0),
                                                              ('Juan Maidana', 27, 'Industrial', 5.2), ('Olivia Peralta', 22, 'Sistemas', 7.6),
                                                              ('Gaspar Medina', 23, 'Mecánica', 7.4), ('Siena Gallardo', 20, 'Química', 8.8),
                                                              ('Mariano Vega', 24, 'Sistemas', 6.7), ('Julia Molina', 21, 'Industrial', 9.3),
                                                              ('Santino Herrera', 22, 'Mecánica', 5.8), ('Almira Flores', 20, 'Química', 7.5),
                                                              ('Ignacio Godoy', 25, 'Sistemas', 8.2), ('Renata Campos', 19, 'Industrial', 8.6),
                                                              ('Francisco Ledesma', 23, 'Mecánica', 6.1), ('Catalina Espinoza', 21, 'Sistemas', 7.7),
                                                              ('Manuel Cabrera', 24, 'Química', 7.2), ('Violeta Prato', 20, 'Industrial', 9.0),
                                                              ('Pedro Núñez', 22, 'Sistemas', 6.3), ('Amparo Ramos', 22, 'Mecánica', 8.1),
                                                              ('Felipe Delgado', 26, 'Química', 5.5), ('Lola Ibáñez', 19, 'Sistemas', 9.4),
                                                              ('Benjamín Gatti', 23, 'Industrial', 7.0), ('Juana Ojeda', 21, 'Mecánica', 7.9),
                                                              ('Marcos Soria', 24, 'Química', 6.6), ('Guadalupe Vidal', 20, 'Sistemas', 8.3),
                                                              ('Lorenzo Ferrari', 22, 'Industrial', 6.0), ('Sara Montenegro', 25, 'Mecánica', 8.8),
                                                              ('Alejo Rossi', 21, 'Química', 7.4), ('Martina Paz', 19, 'Sistemas', 9.7);