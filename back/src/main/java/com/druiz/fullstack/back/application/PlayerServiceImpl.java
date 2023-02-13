package com.druiz.fullstack.back.application;

import com.druiz.fullstack.back.application.port.PlayerService;
import com.druiz.fullstack.back.config.exceptions.NotFoundException;
import com.druiz.fullstack.back.infrastructure.controller.dto.output.JugadorOutputDto;
import com.druiz.fullstack.back.infrastructure.repo.TableroRepo;
import com.druiz.fullstack.back.infrastructure.repo.PlayerRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    PlayerRepo playerRepo;

    @Autowired
    TableroRepo tableroRepo;

    @Override
    public Flux<JugadorOutputDto> findAllPlayers() {
        return playerRepo.findAll()
                .map(JugadorOutputDto::new)
                .doOnNext(p -> log.info("procesando: " + p.toString()))
                .switchIfEmpty(Flux.error(new NotFoundException("Not found players")));
    }
    @Override
    public Mono<JugadorOutputDto> findPlayerById(int idPlayer){
        return playerRepo.findById(idPlayer)
                .map(JugadorOutputDto::new)
                .switchIfEmpty(Mono.error(new NotFoundException("Not found players with id " + idPlayer)));
    }
    @Override
    public Mono<JugadorOutputDto> findPlayerByUserPlayer(String userPlayer){
        return playerRepo.findByUserPlayer(userPlayer)
                .map(JugadorOutputDto::new)
                .switchIfEmpty(Mono.error(new NotFoundException("Not found players with user " + userPlayer)));
    }

    @Override
    public Mono<Void> deletePlayerById(int playerId) {
        return playerRepo.deleteById(playerId);
    }
}
