package com.habesha.lottery_ticket_service.controller;

import com.habesha.lottery_ticket_service.model.TicketModel;
import com.habesha.lottery_ticket_service.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/lottery/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping("/purchase")
    public ResponseEntity<TicketModel> purchaseTicket(@RequestBody TicketModel request) {
        TicketModel response = ticketService.purchaseTicket(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/player")
    public ResponseEntity<List<TicketModel>> getTicketsByPlayer(@RequestParam UUID Player_Id) {
        List<TicketModel> tickets = ticketService.getTicketsByPlayer();
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    @GetMapping("/draw/{drawId}")
    public ResponseEntity<List<TicketModel>> getTicketsByDraw(@PathVariable UUID drawId) {
        List<TicketModel> tickets = ticketService.getTicketsByDraw(drawId);
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }
}