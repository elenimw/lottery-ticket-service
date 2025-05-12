package com.habesha.lottery_ticket_service.repository;

import com.habesha.lottery_ticket_service.dao.TicketDao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TicketRepository extends JpaRepository<TicketDao, UUID> {
    List<TicketDao> findByPlayerId(UUID player_Id);
    List<TicketDao> findByDrawId(UUID draw_Id);
}
