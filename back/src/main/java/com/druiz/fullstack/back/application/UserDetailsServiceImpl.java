package com.druiz.fullstack.back.application;

import com.druiz.fullstack.back.domain.User;
import com.druiz.fullstack.back.infrastructure.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements ReactiveUserDetailsService {

   private final UserRepo userRepository;


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
              .map(this::toUserDetails)
              .onErrorMap(ex -> new RuntimeException("Error retrieving user", ex));
   }

   /**
    * Convierte un User de la base de datos en un UserDetails para la autenticación de Spring Security.
    *
    * @param user usuario de la base de datos
    * @return UserDetails construido a partir del usuario
    */
   private UserDetails toUserDetails(User user) {
      List<GrantedAuthority> authorities = user.getRoles().stream()
              .map(role -> new SimpleGrantedAuthority(role.getName()))
              .collect(Collectors.toList());

      return User.builder()
              .username(user.getUsername())
              .password(user.getPassword())
              .roles(authorities)
              .build();
   }
}


