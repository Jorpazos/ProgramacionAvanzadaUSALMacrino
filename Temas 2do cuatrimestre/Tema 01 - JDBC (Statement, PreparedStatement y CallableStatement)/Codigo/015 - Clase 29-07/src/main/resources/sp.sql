CREATE PROCEDURE sp_procesar_beca(
    IN p_estudiante_id INT,
    IN p_monto_solicitado DECIMAL(10,2),
    OUT p_resultado VARCHAR(100)
)
BEGIN
    DECLARE v_promedio DOUBLE;

    -- Obtener el promedio del estudiante
SELECT promedio INTO v_promedio
FROM estudiantes
WHERE id = p_estudiante_id;

-- Lógica de negocio
IF v_promedio IS NULL THEN
        SET p_resultado = 'ERROR: El estudiante no existe.';
    ELSEIF v_promedio >= 8.0 THEN
        -- Si califica, podríamos insertar en una tabla de becas o actualizar datos
        SET p_resultado = CONCAT('APROBADO: Beca otorgada por $', p_monto_solicitado);
ELSE
        SET p_resultado = CONCAT('RECHAZADO: Promedio insuficiente (', v_promedio, '). Requiere >= 8.0');
END IF;
END

DELIMITER ;