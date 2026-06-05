package upc.ecovolt.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_has_rol") // Nombre de tu tabla en Postgres
public class UsuarioHasRol {

    @EmbeddedId
    private UsuarioHasRolPK usuarioHasRolPk;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false, insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_rol", nullable = false, insertable = false, updatable = false)
    private Role role;

}