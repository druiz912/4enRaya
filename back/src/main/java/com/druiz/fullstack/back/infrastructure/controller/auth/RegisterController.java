package com.druiz.fullstack.back.infrastructure.controller.auth;

import org.springframework.stereotype.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

// Controlador para la página de registro
@Controller
public class RegisterController {

    // Inyecta el servicio de autenticación
    @Autowired
    private AuthenticationService authenticationService;

    // Muestra la página de registro
    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    // Recibe los datos del formulario de registro
    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password, @RequestParam String confirmPassword, Model model) {
        // Intenta registrar al usuario con los datos proporcionados
        if (authenticationService.register(username, password, confirmPassword)) {
            // Si el registro fue exitoso, redirige al usuario al juego
            return "redirect:/game";
        } else {
            // Si el registro no fue exitoso, muestra un mensaje de error
            model.addAttribute("error", "Error al registrarse. Inténtalo de nuevo");
            return "register";
        }
    }
}
