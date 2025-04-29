package com.habesha.lottery_ticket_service.service;

import com.habesha.lottery_ticket_service.dao.PlayersDao;
import com.habesha.lottery_ticket_service.exception.LotteryException;
import com.habesha.lottery_ticket_service.model.PlayersModel;
import com.habesha.lottery_ticket_service.model.UserLoginModel;
import com.habesha.lottery_ticket_service.repository.PlayersRepository;

import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class PlayersService {

    private static final Logger logger = LoggerFactory.getLogger(PlayersService.class);

    @Autowired
    private PlayersRepository playersRepository;

    @Autowired
    private ModelMapper modelMapper;

    public PlayersModel registerPlayer(PlayersModel playersModel) {
        try {
            // Validate input
            if (playersModel == null) {
                logger.error("Player data is null");
                throw new LotteryException("Player data cannot be null", HttpStatus.BAD_REQUEST);
            }

            // Check for existing email or username
            if (playersRepository.existsByEmail(playersModel.getEmail())) {
                logger.warn("Attempt to register with existing email: {}", playersModel.getEmail());
                throw new LotteryException("Email already exists", HttpStatus.BAD_REQUEST);
            }
            if (playersRepository.existsByUsername(playersModel.getUsername())) {
                logger.warn("Attempt to register with existing username: {}", playersModel.getUsername());
                throw new LotteryException("Username already exists", HttpStatus.BAD_REQUEST);
            }

            // Set default values
            playersModel.setCreatedAt(LocalDateTime.now());
            if (playersModel.getBalance() == null) {
                playersModel.setBalance(BigDecimal.ZERO);
            }

            // Map to entity
            PlayersDao entity = modelMapper.map(playersModel, PlayersDao.class);

            // Save to database
            PlayersDao savedEntity = playersRepository.save(entity);
            logger.info("Player registered successfully: {}", savedEntity.getPlayerId());

            // Map back to DTO
            return modelMapper.map(savedEntity, PlayersModel.class);
        } catch (MappingException e) {
            logger.error("Error mapping player data: {}", e.getMessage(), e);
            throw new LotteryException("Error mapping player data: " + e.getMessage(), e);
        } catch (DataIntegrityViolationException e) {
            logger.error("Database constraint violation: {}", e.getMessage(), e);
            throw new LotteryException("Database error: Duplicate entry or constraint violation", e);
        } catch (DataAccessException e) {
            logger.error("Database access error: {}", e.getMessage(), e);
            throw new LotteryException("Database error: Unable to access database or table missing", e);
        } catch (LotteryException e) {
            throw e; // Re-throw custom exceptions
        } catch (Exception e) {
            logger.error("Unexpected error during player registration: {}", e.getMessage(), e);
            throw new LotteryException("Unexpected error during player registration: " + e.getMessage(), e);
        }


    }

    public boolean login(String email, String password) {
        try {
            return playersRepository.findByEmail(email)
                    .map(player -> {
                        boolean isValid = player.getPassword().equals(password);
                        if (isValid) {
                            logger.info("Successful login for email: {}", email);
                        } else {
                            logger.warn("Failed login attempt for email: {}", email);
                        }
                        return isValid;
                    })
                    .orElseGet(() -> {
                        logger.warn("No player found with email: {}", email);
                        return false;
                    });
        } catch (Exception e) {
            logger.error("Error during login for email {}: {}", email, e.getMessage(), e);
            throw new LotteryException("Error during login: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}