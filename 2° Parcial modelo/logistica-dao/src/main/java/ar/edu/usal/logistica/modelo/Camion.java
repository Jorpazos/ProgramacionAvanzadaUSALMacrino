package ar.edu.usal.logistica.modelo;

import ar.edu.usal.logistica.excepcion.ValidacionException;

import java.util.Locale;

/** Camion de la flota. */
public class Camion {

    private Long id;
    private String marca;
    private String modelo;
    private String dominio;
    private double toneladasMaximas;
    private double capacidadTanqueLitros;
    private double consumoLitrosPorKm;

    public Camion(Long id, String marca, String modelo, String dominio,
                  double toneladasMaximas, double capacidadTanqueLitros, double consumoLitrosPorKm) {
        this.id = id;
        this.marca = marca == null ? null : marca.trim();
        this.modelo = modelo == null ? null : modelo.trim();
        // El dominio (patente) se normaliza siempre en mayusculas y sin espacios
        this.dominio = dominio == null ? null : dominio.trim().toUpperCase(Locale.ROOT).replace(" ", "");
        this.toneladasMaximas = toneladasMaximas;
        this.capacidadTanqueLitros = capacidadTanqueLitros;
        this.consumoLitrosPorKm = consumoLitrosPorKm;
    }

    public void validar() throws ValidacionException {
        if (marca == null || marca.isEmpty() || marca.length() > 40) {
            throw new ValidacionException("La marca es obligatoria (máximo 40 caracteres).");
        }
        if (modelo == null || modelo.isEmpty() || modelo.length() > 40) {
            throw new ValidacionException("El modelo es obligatorio (máximo 40 caracteres).");
        }
        // Formato argentino vigente (AB123CD) o anterior (ABC123)
        if (dominio == null || !dominio.matches("[A-Z]{2}\\d{3}[A-Z]{2}|[A-Z]{3}\\d{3}")) {
            throw new ValidacionException("El dominio debe tener formato AB123CD o ABC123.");
        }
        if (toneladasMaximas <= 0 || toneladasMaximas > 99) {
            throw new ValidacionException("Las toneladas máximas deben ser mayores a 0 y menores a 100.");
        }
        if (capacidadTanqueLitros <= 0 || capacidadTanqueLitros > 9999) {
            throw new ValidacionException("La capacidad del tanque debe ser mayor a 0 litros.");
        }
        if (consumoLitrosPorKm <= 0 || consumoLitrosPorKm > 99) {
            throw new ValidacionException("El consumo por km debe ser mayor a 0 litros.");
        }
    }

    /** Litros de combustible necesarios para recorrer una cantidad de km. */
    public double litrosNecesarios(int km) {
        return km * consumoLitrosPorKm;
    }

    /** Descripcion corta para listas: "Scania R450 (AB123CD) - 28 t". */
    public String getDescripcion() {
        return String.format(Locale.ROOT, "%s %s (%s) - %.1f t", marca, modelo, dominio, toneladasMaximas);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMarca() { return marca; }
    public String getModelo() { return modelo; }
    public String getDominio() { return dominio; }
    public double getToneladasMaximas() { return toneladasMaximas; }
    public double getCapacidadTanqueLitros() { return capacidadTanqueLitros; }
    public double getConsumoLitrosPorKm() { return consumoLitrosPorKm; }
}
