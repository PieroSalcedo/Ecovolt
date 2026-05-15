package upc.ecovolt.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/*
 * CRITERIO TÉCNICO: @Embeddable indica que esta clase se "empotrará"
 * dentro de otra entidad como su identificador.
 * Debe implementar Serializable para que JPA pueda manejar la sesión.
 */
@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class RoleHasOptionPK implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "id_rol")
    private Integer idRol;

    @Column(name = "id_option")
    private Integer idOption;
}