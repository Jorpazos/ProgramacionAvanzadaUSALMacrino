package ar.edu.usal.logistica.modelo;

/**
 * Ciudades a las que transporta la empresa (y unicas validas como origen/destino).
 * El nombre del enum (name()) es el codigo que se guarda en la base de datos.
 */
public enum Destino {
    CABA("CABA"),
    CORDOBA("Córdoba"),
    CORRIENTES("Corrientes"),
    FORMOSA("Formosa"),
    LA_PLATA("La Plata"),
    LA_RIOJA("La Rioja"),
    MENDOZA("Mendoza"),
    NEUQUEN("Neuquén");

    private final String nombre;

    Destino(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
}
