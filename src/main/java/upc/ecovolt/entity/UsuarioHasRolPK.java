package upc.ecovolt.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class UsuarioHasRolPK implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "id_user")
    private int idUser;

    @Column(name = "id_rol")
    private int idRol;

}