package com.druiz.fullstack.back.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Clase que representa un rol en el sistema, que puede ser asignado a los usuarios.
 * La interfaz GrantedAuthority es una interfaz de Spring Security se utiliza para representar
 * los roles que un usuario puede tener en una aplicación.
 *
 * La clase Role implementa la interfaz GrantedAuthority porque un objeto de tipo Role representa precisamente un rol.
 */
@Data
@Table(name = "roles")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role implements GrantedAuthority {

    /**
     * Identificador único del rol.
     */
    @Id
    private String id;

    /**
     * Nombre del rol.
     */
    private String name;

    /**
     * Obtiene el nombre del rol como autoridad de seguridad.
     * La implementación del método getAuthority() en Role simplemente devuelve el nombre del rol,
     *  que es lo que se espera de un objeto que implementa GrantedAuthority.
     * @return El nombre del rol como autoridad de seguridad.
     */
    @Override
    public String getAuthority() {
        return name;
    }
}
