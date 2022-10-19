package com.druiz.fullstack.front.infrastructure;

import com.druiz.fullstack.front.application.BoardFeign;
import com.druiz.fullstack.front.domain.Board;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.result.view.Rendering;
import org.thymeleaf.spring5.context.webflux.IReactiveDataDriverContextVariable;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@CrossOrigin
@Slf4j
public class BoardController {

    private final String titleGame = "4 CONNECT GAME";

    @Autowired
    BoardFeign boardFeign;

    @GetMapping("/")
    public String index(Model model) {
        // loads 1 and display 1, stream data, data driven mode.
        IReactiveDataDriverContextVariable reactiveDataDriverMode =
                new ReactiveDataDriverContextVariable(boardFeign.getBoards());

        log.info("*****DATOS -->" + reactiveDataDriverMode);
        // TODO: revisar
        model.addAttribute("titleGame", titleGame);
        model.addAttribute("boards", reactiveDataDriverMode);

        // classic, wait repository loaded all and display it.
        // model.addAttribute("boards", boardsRepo.findAll());

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