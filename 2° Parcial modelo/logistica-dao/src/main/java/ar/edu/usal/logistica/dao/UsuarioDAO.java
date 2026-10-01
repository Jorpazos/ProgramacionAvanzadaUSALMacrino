package ar.edu.usal.logistica.dao;

import ar.edu.usal.logistica.excepcion.DAOException;
import ar.edu.usal.logistica.modelo.Usuario;

import java.time.LocalDateTime;

/** Contrato de acceso a datos de usuarios y de los tokens de "recordarme". */
public interface UsuarioDAO {

    /** @return el usuario o null si no existe */
    Usuario buscarPorUsername(String username) throws DAOException;

    void guardarToken(String token, long usuarioId, LocalDateTime expira) throws DAOException;

    /** @return el usuario dueno de un token vigente, o null si no existe o vencio */
    Usuario buscarPorToken(String token) throws DAOException;

    void eliminarToken(String token) throws DAOException;
}
