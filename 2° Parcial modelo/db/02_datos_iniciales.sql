-- =====================================================================
-- Datos iniciales de demostracion (ejecutar despues de 01_ddl_logistica.sql)
--   Administrador:  admin    / admin123
--   Choferes:       30111222 / chofer123   (Juan Perez, categoria C)
--                   28555666 / chofer123   (Maria Gomez, categoria B)
-- Las contrasenas estan guardadas con PBKDF2 (nunca en texto plano).
-- =====================================================================
USE logistica;

INSERT INTO camion (marca, modelo, dominio, toneladas_max, capacidad_tanque_litros, consumo_litros_km) VALUES
    ('Scania',        'R450',      'AB123CD', 28.00, 600.00, 0.400),
    ('Mercedes-Benz', 'Actros',    'AC456EF', 25.00, 500.00, 0.350),
    ('Iveco',         'Tector',    'AA789GH', 12.00, 250.00, 0.250),
    ('Volkswagen',    'Delivery',  'AD321IJ',  8.00, 150.00, 0.180);

INSERT INTO chofer (nombre, apellido, dni, fecha_nacimiento, categoria, telefono) VALUES
    ('Juan',  'Pérez', '30111222', '1985-03-14', 'C', '1155667788'),
    ('María', 'Gómez', '28555666', '1982-11-02', 'B', '1133445566');

-- Autorizaciones: Juan maneja los tres camiones grandes; Maria, los dos chicos
INSERT INTO chofer_camion (chofer_id, camion_id) VALUES
    (1, 1), (1, 2), (1, 3),
    (2, 3), (2, 4);

INSERT INTO usuario (username, password_hash, rol, chofer_id) VALUES
    ('admin',    '65536$q7KabaZiTaaUIXU3GuIWyA==$Qv970io5LXoUGDKhrRQ1UNQnD9lu+00sGrfGKYzx3gs=', 'ADMIN', NULL),
    ('30111222', '65536$K/tW0Ur6sCQq1mREstBWIw==$uf7wAX1zXDItYLTQapJ4kXidRl/xFZBbQbtQeCQUnzM=', 'CHOFER', 1),
    ('28555666', '65536$K/tW0Ur6sCQq1mREstBWIw==$uf7wAX1zXDItYLTQapJ4kXidRl/xFZBbQbtQeCQUnzM=', 'CHOFER', 2);
