package com.habesha.lottery_ticket_service.repository;
import com.habesha.lottery_ticket_service.dao.PlayersDao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface PlayersRepository extends JpaRepository<PlayersDao, UUID> {
    //methods
    PlayersDao findByEmail(String email);



}
