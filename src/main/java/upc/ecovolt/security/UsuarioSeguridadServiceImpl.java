package upc.ecovolt.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import upc.ecovolt.entity.Option;
import upc.ecovolt.entity.Role;
import upc.ecovolt.entity.User;
import upc.ecovolt.repository.UserRepository;

import java.util.List;

@Service
public class UsuarioSeguridadServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + login));

        List<Role> roles = userRepository.traerRolesDeUsuario(user.getId());
        List<Option> opciones = userRepository.traerEnlacesDeUsuario(user.getId());

        return UsuarioPrincipal.build(user, roles, opciones);
    }
}