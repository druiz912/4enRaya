package com.druiz.fullstack.back.infrastructure.controller.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

// Controlador para la página de inicio de sesión
@Controller
public class LoginController {

    // Inyecta el servicio de autenticación
    @Autowired
    private AuthService authenticationService;

    // Muestra la página de inicio de sesión
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    // Recibe los datos del formulario de inicio de sesión
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model) {
        // Intenta iniciar sesión con los datos proporcionados
        if (authenticationService.login(username, password)) {
            // Si el inicio de sesión fue exitoso, redirige al usuario al juego
            return "redirect:/game";
        } else {
            // Si el inicio de sesión no fue exitoso, muestra un mensaje de error
            model.addAttribute("error", "Nombre de usuario o contraseña incorrectos");
            return "login";
        }
    }
}
