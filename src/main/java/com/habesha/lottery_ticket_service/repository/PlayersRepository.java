package com.habesha.lottery_ticket_service.repository;

import com.habesha.lottery_ticket_service.dao.PlayersDao;
import com.habesha.lottery_ticket_service.model.PlayersModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PlayersRepository extends JpaRepository<PlayersDao, UUID> {
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    Optional<PlayersDao> findByEmail(String email);
}
