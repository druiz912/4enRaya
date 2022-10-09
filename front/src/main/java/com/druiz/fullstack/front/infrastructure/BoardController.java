package com.druiz.fullstack.front.infrastructure;

import com.druiz.fullstack.front.application.BoardFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.spring5.context.webflux.IReactiveDataDriverContextVariable;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;

@Controller
@CrossOrigin
public class BoardController {

    private final String titleGame = "4 CONNECT GAME";

    @Autowired
    BoardFeign boardFeign;


    @GetMapping("/")
    public String index(Model model) {

        IReactiveDataDriverContextVariable reactiveDataDriverMode =
                new ReactiveDataDriverContextVariable(boardFeign.getBoards());

        model.addAttribute("titleGame", titleGame);
        model.addAttribute("boards", reactiveDataDriverMode);

        model.addAttribute("turn", "Turno");
        model.addAttribute("newGame", "Nueva partida");
        model.addAttribute("btnJoin", "Unirse");

        return "index";
    }

    @RequestMapping("/board/{id}")
    public String boardGame(Model model) {

        model.addAttribute("titleGame", titleGame);

        model.addAttribute("turn", "Turno");
        model.addAttribute("newGame", "Nueva partida");
        model.addAttribute("btnJoin", "Unirse");

        return "board";
    }
}