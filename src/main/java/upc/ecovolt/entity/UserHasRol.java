package upc.ecovolt.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users_has_roles") // Nombre de tu tabla en Postgres
public class UserHasRol {

    @EmbeddedId
    private UserHasRolPK userHasRolPk;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false, insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_rol", nullable = false, insertable = false, updatable = false)
    private Role role;

}