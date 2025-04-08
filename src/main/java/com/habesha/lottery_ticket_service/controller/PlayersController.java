package com.habesha.lottery_ticket_service.controller;

import com.habesha.lottery_ticket_service.model.PlayersModel;
import com.habesha.lottery_ticket_service.service.PlayersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/players")
public class PlayersController {

    @Autowired
    private PlayersService playersService;

    @GetMapping("/")
    public String hello() {
        return "Hello, World!";
    }

    // Create a new player
    @PostMapping
    public PlayersModel createPlayer(@RequestBody PlayersModel PlayersModel) {

        return playersService.createPlayer(PlayersModel);
    }

    // Get all players
    @GetMapping
    public List<PlayersModel> getAllPlayers() {
        return playersService.getAllPlayers();
    }

    // Get a player by ID
    @GetMapping("/{playerId}")
    public Optional<PlayersModel> getPlayerById(@PathVariable UUID playerId) {
        return playersService.getPlayerById(playerId);
    }


    // Update a player
    @PutMapping("/{playerId}")
    public PlayersModel updatePlayer(@PathVariable UUID playerId, @RequestBody PlayersModel PlayersModel) {
    //    return playersService.updatePlayer(playerId, PlayersModel);
        return null;
    }

    // Delete a player
    @DeleteMapping("/{playerId}")
    public void deletePlayer(@PathVariable UUID playerId) {
        playersService.deletePlayer(playerId);
    }
}
