package ar.edu.usal.logistica.modelo;

/**
 * Resultado del calculo de un viaje (objeto inmutable).
 *
 * @param distanciaKm     kilometros entre origen y destino
 * @param dias            dias de viaje (a 200 km por dia)
 * @param litrosTotales   combustible necesario para todo el recorrido
 * @param tanques         cantidad de tanques a llenar (redondeado hacia arriba)
 */
public record EstimacionViaje(int distanciaKm, int dias, double litrosTotales, int tanques) {
}
