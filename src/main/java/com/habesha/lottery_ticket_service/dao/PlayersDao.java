package com.habesha.lottery_ticket_service.dao;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data //Lombok will automatically generate getter, setter, to String
@Table(name="players")
public class PlayersDao {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "player_id")
    private UUID player_id;

    @Column(name = "username")
    private String username;

    @Column(unique = true)
    private String email;

    @Column(name="balance")
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name ="created_at")
    private LocalDateTime created_at;


    public void setPlayerId(UUID uuid) {
    }
}
