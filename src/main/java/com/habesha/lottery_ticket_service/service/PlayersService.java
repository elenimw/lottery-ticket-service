package com.habesha.lottery_ticket_service.service;

import com.habesha.lottery_ticket_service.dao.PlayersDao;
import com.habesha.lottery_ticket_service.exception.LotteryException;
import com.habesha.lottery_ticket_service.model.PlayersModel;
import com.habesha.lottery_ticket_service.repository.PlayersRepository;

import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PlayersService {
    private static final Logger logger = LoggerFactory.getLogger(PlayersService.class);

    @Autowired
    private PlayersRepository playersRepository;

    @Autowired
    private ModelMapper modelMapper;

    public PlayersService() {
        // Constructor for Spring to instantiate the bean
    }

    @Autowired
    public void PlayerLoginService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        // Configure ModelMapper to skip the password field
        modelMapper.addMappings(new PropertyMap<PlayersDao, PlayersModel>() {
            @Override
            protected void configure() {
                skip(destination.getPassword());
            }
        });
    }

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

    public PlayersModel login(String email, String password) throws LotteryException {
        try {
            Optional<PlayersDao> player = playersRepository.findByEmail(email);

            if (player.isPresent()) {
                PlayersDao playersDao = player.get();
                if (verifyPassword(password, playersDao.getPassword())) {
                    logger.info("Successful login for email: {}", email);
                    return modelMapper.map(playersDao, PlayersModel.class);
                } else {
                    logger.warn("Incorrect password for email: {}", email);
                    throw new LotteryException("Incorrect email or password", HttpStatus.UNAUTHORIZED);
                }

            } else {
                //If passowrd and username missmatched
                logger.warn("Player not found for email: {}", email);
                throw new LotteryException("Incorrect email or password", HttpStatus.UNAUTHORIZED);
            }
        } catch (LotteryException e) {
            logger.error("Error during login for email {}: {}", email, e.getMessage(), e);
            throw e; // Re-throw to preserve specific status code

        } catch (IllegalArgumentException e) {
            logger.error("Invalid input data for email {}: {}", email, e.getMessage(), e);
            throw new LotteryException("Invalid login data: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("Unexpected error during login for email {}: {}", email, e.getMessage(), e);
            throw new LotteryException("Unexpected error during login", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private boolean verifyPassword(String inputPassword, String storedPassword) throws NoSuchAlgorithmException {
        // Placeholder for secure password verification (e.g., using bcrypt or similar)
        // Implement actual password hashing/checking logic here
        if (inputPassword == null || storedPassword == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }
        return inputPassword.equals(storedPassword); // Replace with secure comparison
    }
}