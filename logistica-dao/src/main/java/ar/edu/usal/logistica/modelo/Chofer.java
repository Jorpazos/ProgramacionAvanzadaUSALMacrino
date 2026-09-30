package ar.edu.usal.logistica.modelo;

import ar.edu.usal.logistica.excepcion.ValidacionException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Chofer de la empresa. Hereda los datos personales de Persona y agrega su
 * categoria y los camiones que esta autorizado a manejar.
 */
public class Chofer extends Persona {

    private Categoria categoria;
    // Lista interna modificable solo a traves de autorizar(); se expone de solo lectura
    private final List<Camion> camionesAutorizados = new ArrayList<>();

    public Chofer(Long id, String nombre, String apellido, String dni,
                  LocalDate fechaNacimiento, Categoria categoria, String telefono) {
        super(id, nombre, apellido, dni, fechaNacimiento, telefono);
        this.categoria = categoria;
    }

    @Override
    public void validar() throws ValidacionException {
        super.validar();
        if (categoria == null) {
            throw new ValidacionException("La categoría es obligatoria.");
        }
        for (Camion camion : camionesAutorizados) {
            if (!categoria.admite(camion.getToneladasMaximas())) {
                throw new ValidacionException("La categoría " + categoria + " no permite manejar el camión "
                        + camion.getDescripcion() + ".");
            }
        }
    }

    /** Agrega un camion a la lista de autorizados (sin repetir). */
    public void autorizar(Camion camion) {
        if (!camionesAutorizados.contains(camion)
                && camionesAutorizados.stream().noneMatch(c -> c.getId() != null && c.getId().equals(camion.getId()))) {
            camionesAutorizados.add(camion);
        }
    }

    /** Un chofer puede manejar un camion si esta autorizado y su categoria alcanza. */
    public boolean puedeManejar(Camion camion) {
        boolean autorizado = camionesAutorizados.stream()
                .anyMatch(c -> c.getId() != null && c.getId().equals(camion.getId()));
        return autorizado && categoria.admite(camion.getToneladasMaximas());
    }

    public boolean estaAutorizadoPara(long camionId) {
        return camionesAutorizados.stream().anyMatch(c -> c.getId() != null && c.getId() == camionId);
    }

    public Categoria getCategoria() { return categoria; }
    public List<Camion> getCamionesAutorizados() { return Collections.unmodifiableList(camionesAutorizados); }
}
