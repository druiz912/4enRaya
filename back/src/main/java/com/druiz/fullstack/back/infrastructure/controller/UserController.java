package com.druiz.fullstack.back.infrastructure.controller;

import com.druiz.fullstack.back.application.UserDetailsServiceImpl;
import com.druiz.fullstack.back.domain.Role;
import com.druiz.fullstack.back.domain.User;
import com.druiz.fullstack.back.infrastructure.controller.dto.input.UserInputDto;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class UserController {

   private final UserDetailsServiceImpl userService;

   /**
    * Constructor para la clase UserController
    *
    * @param userService servicio para detalles de usuario
    */
   public UserController(UserDetailsServiceImpl userService) {
      this.userService = userService;
   }

   /**
    * Registra un nuevo usuario
    *
    * @param user objeto User que contiene el nombre de usuario y la contraseña
    * @return Mono que emite el usuario registrado
    * @throws ResponseStatusException si el usuario ya está registrado
    */
   @PostMapping("/register")
   public Mono<User> register(@RequestBody User user) {
      List<Role> roles = new ArrayList<>();
      roles.add(new Role("ROLE_USER", "Usuario"));
      user.setRoles(roles);
      return userService.register(user.getUsername(), user.getPassword(), user.getRoles())
              .onErrorMap(DuplicateKeyException.class, e -> new ResponseStatusException(HttpStatus.CONFLICT, "Usuario ya registrado"));
   }

   /**
    * Autentica a un usuario
    *
    * @param dto objeto UserInputDto que contiene el nombre de usuario y la contraseña
    * @return Mono que emite los detalles del usuario autenticado
    * @throws ResponseStatusException si el usuario no se encuentra o la contraseña es incorrecta
    */
   @PostMapping("/login")
   public Mono<UserDetails> login(@RequestBody UserInputDto dto) {
      return userService.login(dto.getUsername(), dto.getPassword())
              .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado")))
              .onErrorMap(BadCredentialsException.class, e -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Contraseña incorrecta"));
   }

   /**
    * Verifica si un usuario con el nombre de usuario dado está registrado
    *
    * @param username el nombre de usuario a verificar
    * @return Mono que emite true si el usuario está registrado, false en caso contrario
    */
   @GetMapping("/is-registered/{username}")
   public Mono<Boolean> isRegistered(@PathVariable String username) {
      return userService.isRegistered(username);
   }

   /**
    * Autentica a un usuario con el nombre de usuario y contraseña dados
    *
    * @param username el nombre de usuario a autenticar
    * @param password la contraseña a autenticar
    * @return Mono que emite los detalles del usuario autenticado si la autenticación es exitosa, un Mono vacío en caso contrario
    * @throws ResponseStatusException si el usuario no se encuentra o la contraseña es incorrecta
    */
   @PostMapping("/is-authenticated")
   public Mono<UserDetails> isAuthenticated(@RequestParam String username, @RequestParam String password) {
      return userService.isAuthenticated(username, password)
              .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas")));
   }
}


