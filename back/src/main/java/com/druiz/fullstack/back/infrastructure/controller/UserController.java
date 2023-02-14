package com.druiz.fullstack.back.infrastructure.controller;

import com.druiz.fullstack.back.application.UserDetailsServiceImpl;
import com.druiz.fullstack.back.domain.Role;
import com.druiz.fullstack.back.domain.User;
import com.druiz.fullstack.back.infrastructure.controller.dto.input.UserInputDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserDetailsServiceImpl userService;

    public UserController(UserDetailsServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public Mono<User> register(@RequestBody User user) {
        List<Role> roles = new ArrayList<>();
        roles.add(new Role("ROLE_USER", "User"));
        user.setRoles(roles);
        return userService.register(user.getUsername(), user.getPassword(), user.getRoles());
    }

    @PostMapping("/login")
    public Mono<UserDetails> login(@RequestBody UserInputDto dto) {
        return userService.login(dto.getUsername(), dto.getPassword());
    }

    @GetMapping("/welcome")
    public Mono<String> showWelcomePage() {
        return Mono.just("Welcome to the application!");
    }
}

