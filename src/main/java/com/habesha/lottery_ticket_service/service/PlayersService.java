package com.habesha.lottery_ticket_service.service;

import com.habesha.lottery_ticket_service.dao.PlayersDao;
import com.habesha.lottery_ticket_service.model.PlayersModel;
import com.habesha.lottery_ticket_service.repository.PlayersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PlayersService {

    @Autowired
    private PlayersRepository playersRepository;

   //Create a new player
    public PlayersModel createPlayer(PlayersModel playersModel){
   // return playersRepository.save(playersModel);
        return null;
    }

    //Get all players
    public List<PlayersModel> getAllPlayers(){
       // return playersRepository.findAll();
        return null;
    }

    //Get a player by ID
    public Optional<PlayersModel> getPlayerById(UUID playerId){
       // return playersRepository.findById(playerId);
        return null;
    }

    //Update a player
    public PlayersModel updatePlayer(UUID playerId, PlayersDao playersDao){
        if (playersRepository.existsById(playerId)){
            playersDao.setPlayerId(playerId);
           // return playersRepository.save(playersDao);
        }
        return null;
    }

    //Delete a player
    public void deletePlayer(UUID playerId){
        playersRepository.deleteById(playerId);
    }
}
