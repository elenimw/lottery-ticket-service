package com.habesha.lottery_ticket_service.repository;
import com.habesha.lottery_ticket_service.model.Players;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface PlayerRepository extends JpaRepository<Players, UUID> {
    //methods
    Players findByEmail(String email);



}
