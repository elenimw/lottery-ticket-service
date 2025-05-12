package com.habesha.lottery_ticket_service.dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "tickets")
public class TicketDao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID ticketId;

    @NotNull
    private UUID playerId;

    @NotNull
    private UUID drawId;

    @ElementCollection
    @NotNull
    private List<Integer> numbers;

    private String status;

    private double amountPaid;

    private double prizeAmount;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createdAt;

    public void initializeFields(double ticketCost) {
        this.status = "PURCHASED";
        this.amountPaid = ticketCost;
        this.prizeAmount = 0.0;
        this.createdAt = LocalDateTime.now();
    }
}