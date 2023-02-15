package com.druiz.fullstack.back.application;

import com.druiz.fullstack.back.domain.Role;
import com.druiz.fullstack.back.domain.User;
import com.druiz.fullstack.back.infrastructure.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements ReactiveUserDetailsService {

   private final UserRepo userRepository;

   private final PasswordEncoder passwordEncoder;


   /**
    * Busca a un usuario por su nombre de usuario y devuelve un Mono que emite un UserDetails si el usuario es
    * encontrado, o un error si no es encontrado.
    *
    * @param username nombre de usuario a buscar
    * @return Mono que emite un UserDetails si el usuario es encontrado, o un error si no es encontrado
    */

   @Override
   public Mono<UserDetails> findByUsername(String username) {
       return userRepository.findByUsername(username)
               .switchIfEmpty(Mono.error(new UsernameNotFoundException("User not found with username: " + username)))
               .map(user -> new User(user.getUsername(), user.getPassword(), user.getAuthorities()));
   }

   /**
    * Crea un nuevo usuario con los datos proporcionados y lo guarda en el repositorio de usuarios.
    *
    * @param username el nombre de usuario del nuevo usuario
    * @param password la contraseña del nuevo usuario
    * @param roles los roles del nuevo usuario
    * @return un Mono que emite el nuevo usuario
    */
   public Mono<User> register(String username, String password, List<Role> roles) {
       // Encripta la contraseña antes de guardarla en la base de datos
       String encodedPassword = passwordEncoder.encode(password);

       // Crea el objeto User con los datos proporcionados
       User user = new User(username, encodedPassword, roles);

       // Guarda el usuario en el repositorio y devuelve un Mono que emite el usuario guardado
       return userRepository.save(user);
   }

   /**
    * Verifica si un usuario existe y si la contraseña proporcionada coincide con la contraseña almacenada para ese usuario.
    * Si las credenciales son válidas, devuelve un UserDetails que representa al usuario.
    * Si las credenciales no son válidas, devuelve un Mono que emite un AuthenticationException.
    *
    * @param username el nombre de usuario del usuario que intenta iniciar sesión
    * @param password la contraseña proporcionada por el usuario que intenta iniciar sesión
    * @return un Mono que emite un UserDetails si las credenciales son válidas, o un AuthenticationException si no lo son
    */
   public Mono<UserDetails> login(String username, String password) {
       return userRepository.findByUsername(username)
               .switchIfEmpty(Mono.error(new UsernameNotFoundException("User not found with username: " + username)))
               .filter(user -> passwordEncoder.matches(password, user.getPassword()))
               .switchIfEmpty(Mono.error(new BadCredentialsException("Invalid username or password")))
               .map(user -> new User(user.getUsername(), user.getPassword(), user.getAuthorities()));
   }

   /**
     * Checks if a user with the given username is registered
     * @param username the username to check
     * @return Mono emitting true if user is registered, false otherwise
     */
    public Mono<Boolean> isRegistered(String username) {
        return userRepository.findByUsername(username)
                .map(user -> true)
                .defaultIfEmpty(false);
    }

    /**
     * Authenticates a user with the given username and password
     * @param username the username to authenticate
     * @param password the password to authenticate
     * @return Mono emitting the authenticated user details if successful, empty Mono otherwise
     * @throws BadCredentialsException if the password is incorrect
     */
    public Mono<UserDetails> isAuthenticated(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .map(user -> new User(user.getUsername(), user.getPassword(), user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName()))
                        .toList()))
                .cast(UserDetails.class)
                .switchIfEmpty(Mono.error(new BadCredentialsException("Invalid username/password")));
    }


}


