package ar.edu.usal.logistica.dao.jdbc;

import ar.edu.usal.logistica.dao.CamionDAO;
import ar.edu.usal.logistica.dao.ChoferDAO;
import ar.edu.usal.logistica.dao.DAOFactory;
import ar.edu.usal.logistica.dao.DistanciaDAO;
import ar.edu.usal.logistica.dao.UsuarioDAO;
import ar.edu.usal.logistica.dao.ViajeDAO;

/** Fabrica concreta que entrega las implementaciones JDBC de cada DAO. */
public final class JdbcDAOFactory extends DAOFactory {

    private static final JdbcDAOFactory INSTANCIA = new JdbcDAOFactory();

    private JdbcDAOFactory() {
    }

    public static JdbcDAOFactory getInstance() {
        return INSTANCIA;
    }

    @Override
    public ChoferDAO getChoferDAO() {
        return new ChoferDAOJdbc();
    }

    @Override
    public CamionDAO getCamionDAO() {
        return new CamionDAOJdbc();
    }

    @Override
    public UsuarioDAO getUsuarioDAO() {
        return new UsuarioDAOJdbc();
    }

    @Override
    public ViajeDAO getViajeDAO() {
        return new ViajeDAOJdbc();
    }

    @Override
    public DistanciaDAO getDistanciaDAO() {
        return new DistanciaDAOJdbc();
    }
}
