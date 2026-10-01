package ar.edu.usal.logistica.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ar.edu.usal.logistica.excepcion.ValidacionException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class CalculadoraViajeTest {

    // Camion de 30 t, tanque de 400 l, consume 0,4 l/km
    private final Camion camion = new Camion(1L, "Scania", "R450", "ab123cd", 30, 400, 0.4);

    @Test
    void diasSeRedondeanHaciaArribaA200KmPorDia() {
        assertEquals(1, CalculadoraViaje.calcular(camion, 200).dias());
        assertEquals(2, CalculadoraViaje.calcular(camion, 201).dias());
        // CABA - Córdoba: 646 km -> 4 dias
        assertEquals(4, CalculadoraViaje.calcular(camion, 646).dias());
    }

    @Test
    void tanquesSeCalculanConLitrosTotalesYSeRedondeanHaciaArriba() {
        // 1000 km * 0,4 = 400 litros = exactamente 1 tanque
        assertEquals(1, CalculadoraViaje.calcular(camion, 1000).tanques());
        // 1001 km = 400,4 litros = 2 tanques
        assertEquals(2, CalculadoraViaje.calcular(camion, 1001).tanques());
        // Mendoza - CABA: 985 km = 394 litros = 1 tanque
        assertEquals(1, CalculadoraViaje.calcular(camion, 985).tanques());
    }

    @Test
    void distanciaNoPositivaEsInvalida() {
        assertThrows(IllegalArgumentException.class, () -> CalculadoraViaje.calcular(camion, 0));
    }

    @Test
    void elDominioSeNormalizaEnMayusculas() {
        assertEquals("AB123CD", camion.getDominio());
    }

    @Test
    void choferSoloManejaCamionesAutorizadosYDentroDeSuCategoria() throws ValidacionException {
        Chofer chofer = new Chofer(1L, "Juan", "Pérez", "30111222", LocalDate.of(1990, 5, 10), Categoria.B, "1144556677");
        assertEquals(false, chofer.puedeManejar(camion)); // no autorizado
        chofer.autorizar(camion);
        assertEquals(false, chofer.puedeManejar(camion)); // categoria B llega a 20 t y el camion es de 30 t
        assertThrows(ValidacionException.class, chofer::validar);

        Chofer categoriaC = new Chofer(2L, "Ana", "Gómez", "30111223", LocalDate.of(1988, 1, 1), Categoria.C, "1144556677");
        categoriaC.autorizar(camion);
        assertTrue(categoriaC.puedeManejar(camion));
        categoriaC.validar();
    }

    @Test
    void elViajeNoAdmiteMismoOrigenYDestino() {
        Chofer chofer = new Chofer(2L, "Ana", "Gómez", "30111223", LocalDate.of(1988, 1, 1), Categoria.C, "1144556677");
        chofer.autorizar(camion);
        Viaje viaje = Viaje.nuevo(chofer, camion, Destino.CABA, Destino.CABA, 10);
        assertThrows(ValidacionException.class, viaje::validar);
    }
}
