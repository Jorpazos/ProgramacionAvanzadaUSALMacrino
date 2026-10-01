package ar.edu.usal.logistica.modelo;

/**
 * Categoria de licencia del chofer. Cada categoria habilita a manejar
 * camiones hasta cierta cantidad de toneladas.
 */
public enum Categoria {
    A(10), B(20), C(30), D(40);

    private final int toneladasMaximas;

    Categoria(int toneladasMaximas) {
        this.toneladasMaximas = toneladasMaximas;
    }

    public int getToneladasMaximas() {
        return toneladasMaximas;
    }

    /** Indica si un camion de esas toneladas puede ser manejado con esta categoria. */
    public boolean admite(double toneladas) {
        return toneladas <= toneladasMaximas;
    }

    /** Descripcion para mostrar en listas desplegables. */
    public String getDescripcion() {
        return "Categoría " + name() + " (hasta " + toneladasMaximas + " t)";
    }
}
