package ar.edu.usal.logistica.modelo;

import ar.edu.usal.logistica.excepcion.ValidacionException;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

/**
 * Clase abstracta con los datos comunes a cualquier persona del dominio.
 * Hoy solo la especializa Chofer, pero deja el diseno listo para sumar
 * otras personas (por ejemplo, un despachante) sin duplicar codigo.
 */
public abstract class Persona {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private Long id;
    private String nombre;
    private String apellido;
    private String dni;
    private LocalDate fechaNacimiento;
    private String telefono;

    protected Persona(Long id, String nombre, String apellido, String dni,
                      LocalDate fechaNacimiento, String telefono) {
        this.id = id;
        this.nombre = nombre == null ? null : nombre.trim();
        this.apellido = apellido == null ? null : apellido.trim();
        this.dni = dni == null ? null : dni.trim();
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono == null ? null : telefono.trim();
    }

    /** Valida los datos personales. Las subclases la extienden con sus propias reglas. */
    public void validar() throws ValidacionException {
        if (nombre == null || nombre.isEmpty() || nombre.length() > 50) {
            throw new ValidacionException("El nombre es obligatorio (máximo 50 caracteres).");
        }
        if (apellido == null || apellido.isEmpty() || apellido.length() > 50) {
            throw new ValidacionException("El apellido es obligatorio (máximo 50 caracteres).");
        }
        if (dni == null || !dni.matches("\\d{7,8}")) {
            throw new ValidacionException("El DNI debe tener 7 u 8 dígitos numéricos.");
        }
        if (fechaNacimiento == null) {
            throw new ValidacionException("La fecha de nacimiento es obligatoria.");
        }
        if (getEdad() < 18) {
            throw new ValidacionException("El chofer debe ser mayor de 18 años.");
        }
        if (getEdad() > 100) {
            throw new ValidacionException("La fecha de nacimiento no es válida.");
        }
        if (telefono == null || !telefono.matches("\\d{8,15}")) {
            throw new ValidacionException("El teléfono celular debe tener entre 8 y 15 dígitos, sin espacios ni guiones.");
        }
    }

    public int getEdad() {
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }

    public String getNombreCompleto() {
        return apellido + ", " + nombre;
    }

    public String getFechaNacimientoFormateada() {
        return fechaNacimiento == null ? "" : fechaNacimiento.format(FORMATO);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getDni() { return dni; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public String getTelefono() { return telefono; }
}
