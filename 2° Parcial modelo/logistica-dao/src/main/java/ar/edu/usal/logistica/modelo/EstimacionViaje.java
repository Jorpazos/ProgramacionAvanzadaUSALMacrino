package ar.edu.usal.logistica.modelo;

import java.util.Objects;

/**
 * Resultado del calculo de un viaje (objeto inmutable).
 * Los accesores se llaman igual que el atributo (distanciaKm(), dias()...) y no getX(),
 * porque asi los usan las vistas JSP.
 */
public final class EstimacionViaje {

    private final int distanciaKm;
    private final int dias;
    private final double litrosTotales;
    private final int tanques;

    /**
     * @param distanciaKm     kilometros entre origen y destino
     * @param dias            dias de viaje (a 200 km por dia)
     * @param litrosTotales   combustible necesario para todo el recorrido
     * @param tanques         cantidad de tanques a llenar (redondeado hacia arriba)
     */
    public EstimacionViaje(int distanciaKm, int dias, double litrosTotales, int tanques) {
        this.distanciaKm = distanciaKm;
        this.dias = dias;
        this.litrosTotales = litrosTotales;
        this.tanques = tanques;
    }

    public int distanciaKm() { return distanciaKm; }
    public int dias() { return dias; }
    public double litrosTotales() { return litrosTotales; }
    public int tanques() { return tanques; }

    @Override
    public boolean equals(Object otro) {
        if (this == otro) {
            return true;
        }
        if (!(otro instanceof EstimacionViaje)) {
            return false;
        }
        EstimacionViaje e = (EstimacionViaje) otro;
        return distanciaKm == e.distanciaKm && dias == e.dias
                && Double.compare(litrosTotales, e.litrosTotales) == 0 && tanques == e.tanques;
    }

    @Override
    public int hashCode() {
        return Objects.hash(distanciaKm, dias, litrosTotales, tanques);
    }

    @Override
    public String toString() {
        return "EstimacionViaje[distanciaKm=" + distanciaKm + ", dias=" + dias
                + ", litrosTotales=" + litrosTotales + ", tanques=" + tanques + "]";
    }
}
