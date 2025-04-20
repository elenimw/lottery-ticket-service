package com.habesha.lottery_ticket_service.controller;

import com.habesha.lottery_ticket_service.model.PlayersModel;
import com.habesha.lottery_ticket_service.service.PlayersService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lottery")
public class PlayersController {

    @Autowired
    private PlayersService playersService;

    @Operation(summary = "Register a new player")
    @PostMapping("/player/register")
    public ResponseEntity<PlayersModel> registerPlayer(@Valid @RequestBody PlayersModel playersModel) {
        PlayersModel savedPlayer = playersService.registerPlayer(playersModel);
        return new ResponseEntity<>(savedPlayer, HttpStatus.CREATED);
    }

}