package upc.ecovolt.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Utilidad para cifrar contraseñas en formato BCrypt.
 * Úsalo para generar los hashes que insertarás manualmente en la base de datos
 * para tus pruebas iniciales y sustento ante el profesor.
 */
public class EncoderPassword {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // REGLA DE NEGOCIO: Nunca guardar contraseñas en texto plano.
        // Generamos un hash para el usuario administrador de prueba
        String passwordPlano = "piero";

        String encodedPassword = encoder.encode(passwordPlano);

        System.out.println("============================================");
        System.out.println("ECOVOLT - GENERADOR DE SEGURIDAD");
        System.out.println("Contraseña original: " + passwordPlano);
        System.out.println("Hash para la Base de Datos: " + encodedPassword);
        System.out.println("============================================");
    }
}