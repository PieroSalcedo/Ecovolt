package upc.ecovolt.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "options")
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_option")
    private Integer idOption;

    /*
     * REGLA DE NEGOCIO: Etiqueta de Interfaz.
     * Es el nombre que aparecerá en el menú del sistema (Ej: 'Mis Dispositivos', 'Reporte de Consumo').
     */
    @Column(name = "name", length = 100)
    private String nombre;

    /*
     * REGLA DE NEGOCIO: Enrutamiento Dinámico.
     * Define la ruta hacia donde navegará el Frontend (Angular) al hacer clic.
     * También sirve como 'Authority' para proteger rutas en el backend.
     */
    @Column(name = "route", length = 255)
    private String ruta;

    /*
     * REGLA DE NEGOCIO: Categorización de UI.
     * 1: Ítem de Menú principal, 2: Botón de acción, 3: Vista de configuración.
     * Permite al Frontend saber dónde renderizar este permiso.
     */
    @Column(name = "type")
    private Integer tipo;

    /*
     * REGLA DE NEGOCIO: Disponibilidad de funcionalidad.
     * Permite "apagar" una funcionalidad de todo el sistema (ej. mantenimiento)
     * sin borrar los permisos asignados a los roles.
     */
    @Column(name = "status")
    private Integer estado = 1;
}