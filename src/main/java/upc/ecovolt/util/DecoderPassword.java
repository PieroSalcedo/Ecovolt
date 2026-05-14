package upc.ecovolt.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Utilidad para verificar la coincidencia de contraseñas.
 * Útil para depurar errores si sientes que el login no te reconoce la clave.
 */
public class DecoderPassword {

    public static void main(String[] args) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // 1. La contraseña que el usuario escribe en el login (ejemplo)
        String passwordIngresado = "piero";

        // 2. El hash que copiaste de tu base de datos (ejemplo de un hash real)
        String hashEnBaseDatos = "$2a$10$.5VYa9MURD74o48laNGZNumd56qlEN4LaL5VWwz/SArKLrC8WH0jy";

        boolean isMatch = passwordEncoder.matches(passwordIngresado, hashEnBaseDatos);

        System.out.println("============================================");
        System.out.println("ECOVOLT - VALIDADOR DE HASH");
        System.out.println("¿La contraseña coincide?: " + (isMatch ? "SÍ (Autorizado)" : "NO (Error)"));
        System.out.println("============================================");
    }
}