package com.druiz.fullstack.back.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Clase que representa un usuario en la aplicación.
 * Implementa la interfaz UserDetails de Spring Security,
 * lo que permite su uso en la autenticación y autorización de usuarios.
 */
@Table(name = "users")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {
    /**
      * Identificador único del usuario.
      */
     @Id
     private String id;

     /**
      * Nombre de usuario.
      */
     private String username;

     /**
      * Contraseña del usuario.
      */
     private String password;

     /**
      * Lista de roles asociados al usuario.
      */
     @ManyToMany(fetch = FetchType.EAGER)
     private List<Role> roles;

     /**
      * Lista de autoridades/granted authorities asignadas al usuario.
      */
     private List<GrantedAuthority> authorities;


    /**
       * Constructor de la clase User.
       * @param username el nombre de usuario del usuario.
       * @param password la contraseña del usuario.
       * @param authorities la lista de autoridades/granted authorities asignadas al usuario.
       */
      public User(String username, String password, Collection<? extends GrantedAuthority> authorities) {
          this.username = username;
          this.password = password;
          this.authorities = new ArrayList<>(authorities);
      }


    /**
     * Retorna la lista de roles del usuario,
     * la cual es usada por Spring Security para realizar la autorización de acciones en la aplicación.
     *
     * @return la lista de roles del usuario.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    /**
     * Retorna el password del usuario,
     * que es utilizado para la autenticación del mismo en la aplicación.
     *
     * @return el password del usuario.
     */
    @Override
    public String getPassword() {
        return this.password;
    }

    /**
     * Retorna el nombre de usuario del usuario,
     * que es utilizado para la autenticación del mismo en la aplicación.
     *
     * @return el nombre de usuario del usuario.
     */
    @Override
    public String getUsername() {
        return this.username;
    }

    /**
     * Retorna un booleano que indica si la cuenta del usuario ha expirado o no.
     *
     * @return un booleano que indica si la cuenta del usuario ha expirado o no.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Retorna un booleano que indica si la cuenta del usuario ha sido bloqueada o no.
     *
     * @return un booleano que indica si la cuenta del usuario ha sido bloqueada o no.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Retorna un booleano que indica si las credenciales del usuario han expirado o no.
     *
     * @return un booleano que indica si las credenciales del usuario han expirado o no.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Retorna un booleano que indica si el usuario está habilitado o no.
     *
     * @return un booleano que indica si el usuario está habilitado o no.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
