package com.druiz.fullstack.back.infrastructure.controller.dto.auth;

import com.druiz.fullstack.back.domain.User;
import org.springframework.stereotype.Component;

/**
 * La clase Session se utiliza para almacenar información sobre el usuario autenticado.
 */
@Component
public class Session {

    // Atributo para almacenar el usuario autenticado
    private User user;


    /**
     * Devuelve el usuario autenticado.
     * @return el usuario autenticado.
     */
    public User getUser() {
        return user;
    }

    // Establece el usuario autenticado
    public void setUser(User user) {
        this.user = user;
    }
}
