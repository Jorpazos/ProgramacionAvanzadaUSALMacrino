package edu.usal.singleton;

import java.util.HashMap;
import java.util.Map;

/*
    Patron Singleton : Garantizar que exista una unica instancia y de acceso de global

    Reglas del patron:

    1 - Constructor privado
    2 - Atributo de clase estatico
    3 - Metodo publico y estatico de acceso "getInstance()"
 */
public class ConfigSingleton {

    private static ConfigSingleton instancia;
    private boolean flag;

    private Map<String, String> propiedades;

    public ConfigSingleton() {
        this.propiedades = new HashMap<>();

        this.propiedades.put("idioma", "ES");
        this.propiedades.put("version", "1.0.0");
        this.propiedades.put("tema", "Oscuro");
    }

    public static synchronized ConfigSingleton getInstance(){
        if(instancia == null){
            instancia = new ConfigSingleton();
        }

        return instancia;
    }

    /*public Map<String, String> getPropiedades() {
        return propiedades;
    }*/

    public void setPropiedades(Map<String, String> propiedades) {
        this.propiedades = propiedades;
    }

    public String getPropiedad(String key){
        return this.propiedades.get(key);
    }

    public void setPropiedad(String key, String valor){
        this.propiedades.put(key, valor);
    }
}
