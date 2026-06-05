package upc.ecovolt.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "rol_has_option")
public class RoleHasOption {

    /*
     * CRITERIO TÉCNICO: @EmbeddedId define la llave primaria compuesta
     * basada en el objeto RoleHasOptionPK.
     */
    @EmbeddedId
    private RoleHasOptionPK id;

    /*
     * REGLA DE NEGOCIO: Asignación de Permisos.
     * Esta relación permite navegar desde un rol hacia las opciones de menú permitidas.
     */
    @ManyToOne
    @JoinColumn(name = "id_rol", nullable = false, insertable = false, updatable = false)
    private Role role;

    /*
     * REGLA DE NEGOCIO: Granularidad de Funcionalidad.
     * Vincula la funcionalidad específica del sistema con el rol asignado.
     */
    @ManyToOne
    @JoinColumn(name = "id_option", nullable = false, insertable = false, updatable = false)
    private Option option;
}