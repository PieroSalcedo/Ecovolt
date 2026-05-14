package upc.ecovolt.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long id;

    /*
     * REGLA DE NEGOCIO: Identificador único de autenticación.
     * Separamos 'login' de 'email' para permitir mayor flexibilidad
     * (el usuario podría loguearse con un nickname o con su correo).
     */
    @Column(name = "login", nullable = false, unique = true, length = 50)
    private String login;

    /*
     * REGLA DE NEGOCIO: Seguridad de la información.
     * Almacenará el hash (BCrypt), nunca el texto plano.
     */
    @Column(name = "password", nullable = false, length = 255)
    private String password;

    /*
     * REGLA DE NEGOCIO: Canal principal de comunicación y recuperación.
     * Es único para evitar duplicidad de cuentas.
     */
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    /*
     * REGLA DE NEGOCIO: Cuota de Servicio.
     * Todo usuario debe estar vinculado a un plan para determinar sus límites (SaaS).
     */
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_plan", nullable = false)
    private SubscriptionPlan subscriptionPlan;

    /*
     * REGLA DE NEGOCIO: Control de Acceso Basado en Roles (RBAC).
     * Define qué partes de la API de Ecovolt puede consumir (Admin, Analyst, Customer).
     * Usamos EAGER para que al autenticar, los roles se carguen de inmediato.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_has_roles",
            joinColumns = @JoinColumn(name = "id_user"),
            inverseJoinColumns = @JoinColumn(name = "id_rol")
    )
    private Set<Role> roles = new HashSet<>();

    public String getFullName() {
        return (firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "");
    }
}