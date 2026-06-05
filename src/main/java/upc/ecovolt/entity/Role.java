package upc.ecovolt.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private Integer idRol;

    /*
     * REGLA DE NEGOCIO: Identificador de Autoridad.
     * Almacena nombres como 'ROLE_ADMIN', 'ROLE_CUSTOMER'.
     * Es la base para que Spring Security permita o deniegue el acceso a los endpoints.
     */
    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    /*
     * REGLA DE NEGOCIO: Control de Vigencia.
     * 1 = Activo, 0 = Inactivo.
     * Permite deshabilitar un rol completo sin borrarlo de la base de datos (Integridad Referencial).
     */
    @Column(name = "status")
    private Integer status = 1;
}