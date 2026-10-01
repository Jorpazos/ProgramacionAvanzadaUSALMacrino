package ar.edu.usal.logistica.modelo;

/** Ciclo de vida de un viaje: ASIGNADO -> EN_CURSO -> FINALIZADO. */
public enum EstadoViaje {
    ASIGNADO("Asignado"),
    EN_CURSO("En curso"),
    FINALIZADO("Finalizado");

    private final String descripcion;

    EstadoViaje(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    /** Un camion esta ocupado mientras su viaje no haya finalizado. */
    public boolean ocupaCamion() {
        return this != FINALIZADO;
    }
}
