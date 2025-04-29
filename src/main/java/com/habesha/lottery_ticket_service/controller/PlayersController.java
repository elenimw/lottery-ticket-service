package com.habesha.lottery_ticket_service.controller;

import com.habesha.lottery_ticket_service.model.PlayersModel;
import com.habesha.lottery_ticket_service.model.UserLoginModel;
import com.habesha.lottery_ticket_service.service.PlayersService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/lottery")
public class PlayersController {

    private static final Logger logger = LoggerFactory.getLogger(PlayersController.class);

    @Autowired
    private PlayersService playersService;

    @Operation(summary = "Register a new player")
    @PostMapping("/player/register")
    public ResponseEntity<PlayersModel> registerPlayer(@Valid @RequestBody PlayersModel playersModel) {
        logger.info("Received request to register player: {}", playersModel);
        try {
            PlayersModel savedPlayer = playersService.registerPlayer(playersModel);
            logger.info("Successfully registered player with ID: {}", savedPlayer.getPlayerId());
            return new ResponseEntity<>(savedPlayer, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Failed to register player: {}", e.getMessage(), e);
            throw e;
        }
    }
    @Operation(summary = "Login a player")
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody UserLoginModel userLoginModel) {
        logger.info("Received login request for email: {}", userLoginModel.getEmail());
        Map<String, String> response = new HashMap<>();
        if (userLoginModel.getEmail() == null || userLoginModel.getPassword() == null) {
            logger.warn("Login attempt with missing email or password");
            response.put("message", "Email and password are required");
            return ResponseEntity.badRequest().body(response);
        }
//        Authentication authentication = new UsernamePasswordAuthenticationToken(
//                userLoginModel.getEmail(), null, List.of(new SimpleGrantedAuthority("ROLE_USER"))
//        );
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
     boolean isValid = playersService.login(userLoginModel.getEmail(), userLoginModel.getPassword());
        if (isValid) {
            response.put("message", "Login successful");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Invalid email or password");
            return ResponseEntity.badRequest().body(response);
        }


    }

}