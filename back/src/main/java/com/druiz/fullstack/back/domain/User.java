package com.druiz.fullstack.back.domain;

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

@Table(name = "users")
@Builder
@Data
public class User implements UserDetails {
    @Id
    private String id;
    private String username;
    private String password;
    @ManyToMany(fetch = FetchType.EAGER)
     private List<Role> roles;

     // constructores, getters y setters

    // TODO: Cambiar esta lógica
     @Override
     public Collection<? extends GrantedAuthority> getAuthorities() {
         return roles.stream()
                 .map(role -> new SimpleGrantedAuthority(role.getName()))
                 .toList();
     }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
