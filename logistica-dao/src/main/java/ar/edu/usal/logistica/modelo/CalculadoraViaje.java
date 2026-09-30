package ar.edu.usal.logistica.modelo;

/**
 * Logica de calculo del viaje pedida en el enunciado:
 * <ul>
 *   <li>Tiempo: por dia se recorren 200 km, se redondea hacia arriba.</li>
 *   <li>Tanques: litros totales (km x consumo por km) / capacidad del tanque, hacia arriba.</li>
 * </ul>
 * Es una clase sin estado, por eso su metodo es estatico y se puede probar sola.
 */
public final class CalculadoraViaje {

    /** Kilometros que recorre el camion por dia (dato del enunciado). */
    public static final int KM_POR_DIA = 200;

    private CalculadoraViaje() {
        // Clase utilitaria: no se instancia
    }

    public static EstimacionViaje calcular(Camion camion, int distanciaKm) {
        if (distanciaKm <= 0) {
            throw new IllegalArgumentException("La distancia debe ser mayor a cero.");
        }
        int dias = (int) Math.ceil((double) distanciaKm / KM_POR_DIA);
        double litros = camion.litrosNecesarios(distanciaKm);
        int tanques = (int) Math.ceil(litros / camion.getCapacidadTanqueLitros());
        return new EstimacionViaje(distanciaKm, dias, litros, tanques);
    }
}
