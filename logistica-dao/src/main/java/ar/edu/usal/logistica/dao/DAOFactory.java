package ar.edu.usal.logistica.dao;

import ar.edu.usal.logistica.dao.jdbc.JdbcDAOFactory;

/**
 * PATRON FACTORY: la capa web le pide los DAO a la fabrica y solo conoce
 * interfaces. Si manana se cambia JDBC por otra tecnologia, solo se agrega
 * otra fabrica y los controladores no se modifican.
 */
public abstract class DAOFactory {

    /** Fabrica concreta en uso (hoy, la de JDBC). */
    public static DAOFactory getInstance() {
        return JdbcDAOFactory.getInstance();
    }

    public abstract ChoferDAO getChoferDAO();

    public abstract CamionDAO getCamionDAO();

    public abstract UsuarioDAO getUsuarioDAO();

    public abstract ViajeDAO getViajeDAO();

    public abstract DistanciaDAO getDistanciaDAO();
}
