package com.habesha.lottery_ticket_service.model;


import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Data
public class TicketModel {


    private UUID player_id;
    private UUID ticket_id;
    private UUID draw_id;
    private List<Integer> numbers;
    private String status;
    private double amount_paid;
    private double prize_amount;
    private LocalDateTime createdAt;

}
