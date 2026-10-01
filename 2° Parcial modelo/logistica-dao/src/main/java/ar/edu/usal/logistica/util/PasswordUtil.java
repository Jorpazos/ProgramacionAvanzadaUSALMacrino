package ar.edu.usal.logistica.util;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HexFormat;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Manejo seguro de contrasenas: nunca se guardan en texto plano.
 * Se usa PBKDF2 con sal aleatoria. Formato guardado: iteraciones$sal$hash (Base64).
 */
public final class PasswordUtil {

    private static final int ITERACIONES = 65_536;
    private static final int LONGITUD_CLAVE_BITS = 256;
    private static final SecureRandom RANDOM = new SecureRandom();

    private PasswordUtil() {
    }

    public static String hashear(String password) {
        byte[] sal = new byte[16];
        RANDOM.nextBytes(sal);
        byte[] hash = pbkdf2(password, sal, ITERACIONES);
        return ITERACIONES + "$" + Base64.getEncoder().encodeToString(sal)
                + "$" + Base64.getEncoder().encodeToString(hash);
    }

    public static boolean verificar(String password, String guardado) {
        if (password == null || guardado == null) {
            return false;
        }
        String[] partes = guardado.split("\\$");
        if (partes.length != 3) {
            return false;
        }
        byte[] sal = Base64.getDecoder().decode(partes[1]);
        byte[] esperado = Base64.getDecoder().decode(partes[2]);
        byte[] calculado = pbkdf2(password, sal, Integer.parseInt(partes[0]));
        // Comparacion en tiempo constante para evitar ataques de timing
        return MessageDigest.isEqual(esperado, calculado);
    }

    /** Token aleatorio de 64 caracteres hexadecimales para la cookie "recordarme". */
    public static String generarToken() {
        byte[] bytes = new byte[32];
        RANDOM.nextBytes(bytes);
        return HexFormat.of().formatHex(bytes);
    }

    private static byte[] pbkdf2(String password, byte[] sal, int iteraciones) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), sal, iteraciones, LONGITUD_CLAVE_BITS);
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("No se pudo calcular el hash de la contraseña", e);
        }
    }
}
