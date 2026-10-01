package ar.edu.usal.logistica.modelo;

/** Usuario que inicia sesion. Un usuario CHOFER esta ligado a un chofer concreto. */
public class Usuario {

    private final Long id;
    private final String username;
    private final String passwordHash;
    private final Rol rol;
    private final Long choferId;

    public Usuario(Long id, String username, String passwordHash, Rol rol, Long choferId) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.rol = rol;
        this.choferId = choferId;
    }

    public boolean esAdmin() {
        return rol == Rol.ADMIN;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public Rol getRol() { return rol; }
    public Long getChoferId() { return choferId; }
}
