package edu.usal.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * Lee application.properties desde el classpath (src/main/resources).
 * En un WAR NO sirve FileReader con ruta relativa: se usa el ClassLoader.
 */
public class PropertiesUtil {

    private static final String ARCHIVO = "application.properties";
    private static final Properties props = new Properties();

    static {
        try (InputStream in = PropertiesUtil.class.getClassLoader().getResourceAsStream(ARCHIVO)) {
            if (in == null) {
                throw new IllegalStateException("No se encontró " + ARCHIVO + " en el classpath");
            }
            // Se lee como UTF-8 para que los acentos se vean bien
            props.load(new InputStreamReader(in, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new IllegalStateException("Error leyendo " + ARCHIVO, e);
        }
    }

    private PropertiesUtil() {
    }

    public static String get(String key) {
        return props.getProperty(key);
    }

    public static String get(String key, String defecto) {
        return props.getProperty(key, defecto);
    }

    public static double getDouble(String key, double defecto) {
        String v = props.getProperty(key);
        if (v == null || v.trim().isEmpty()) return defecto;
        try {
            return Double.parseDouble(v.trim());
        } catch (NumberFormatException e) {
            return defecto;
        }
    }

    public static int getInt(String key, int defecto) {
        String v = props.getProperty(key);
        if (v == null || v.trim().isEmpty()) return defecto;
        try {
            return Integer.parseInt(v.trim());
        } catch (NumberFormatException e) {
            return defecto;
        }
    }
}
