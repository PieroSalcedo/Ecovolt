package upc.ecovolt.security;

import upc.ecovolt.entity.Option;
import upc.ecovolt.entity.Role;
import upc.ecovolt.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class UsuarioPrincipal implements UserDetails {

    private Long idUser;
    private String login;
    private String password;
    private String fullName;
    private Collection<? extends GrantedAuthority> authorities;
    private List<Option> opciones;

    public static UsuarioPrincipal build(User user, List<Role> roles, List<Option> opciones) {
        List<GrantedAuthority> authorities = roles.stream()
                .map(rol -> new SimpleGrantedAuthority(rol.getName().startsWith("ROLE_")
                        ? rol.getName() : "ROLE_" + rol.getName()))
                .collect(Collectors.toList());

        return new UsuarioPrincipal(
                user.getIdUser(),
                user.getLogin(),
                user.getPassword(),
                user.getFullName(), // Usamos el método de la entidad
                authorities,
                opciones);
    }

    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }
    @Override public String getPassword() { return password; }
    @Override public String getUsername() { return login; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}