//// File: src/test/java/com/habesha/lottery_ticket_service/service/PlayersServiceLoggingTest.java
//package com.habesha.lottery_ticket_service.service;
//
//import ch.qos.logback.classic.Logger;
//import ch.qos.logback.classic.spi.ILoggingEvent;
//import ch.qos.logback.core.read.ListAppender;
//import com.habesha.lottery_ticket_service.dao.PlayersDao;
//import com.habesha.lottery_ticket_service.model.PlayersModel;
//import com.habesha.lottery_ticket_service.repository.PlayersRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.modelmapper.ModelMapper;
//import org.slf4j.LoggerFactory;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class PlayersServiceLoggingTest {
//
//    @Mock
//    private PlayersRepository playersRepository;
//
//    @Mock
//    private ModelMapper modelMapper;
//
//    @InjectMocks
//    private PlayersService playersService;
//
//    private PlayersModel playersModel;
//    private PlayersDao playersDao;
//    private ListAppender<ILoggingEvent> listAppender;
//
//    @BeforeEach
//    void setUp() {
//        // Initialize test data
//        playersModel = new PlayersModel();
//        playersModel.setPlayerId(UUID.randomUUID());
//        playersModel.setUsername("johndoe");
//        playersModel.setEmail("john.doe@example.com");
//        playersModel.setFirstName("John");
//        playersModel.setLastName("Doe");
//        playersModel.setBirthday(LocalDate.of(1990, 1, 1));
//        playersModel.setPhoneNumber("1234567890");
//        playersModel.setPassword("Password@123");
//        playersModel.setBalance(BigDecimal.ZERO);
//        playersModel.setCreatedAt(LocalDateTime.now());
//
//        playersDao = new PlayersDao();
//        playersDao.setPlayerId(playersModel.getPlayerId());
//        playersDao.setUsername(playersModel.getUsername());
//        playersDao.setEmail(playersModel.getEmail());
//        playersDao.setFirstName(playersModel.getFirstName());
//        playersDao.setLastName(playersModel.getLastName());
//        playersDao.setBirthday(playersModel.getBirthday());
//        playersDao.setPhoneNumber(playersModel.getPhoneNumber());
//        playersDao.setPassword(playersModel.getPassword());
//        playersDao.setBalance(playersModel.getBalance());
//        playersDao.setCreatedAt(playersModel.getCreatedAt());
//
//        // Set up Logback ListAppender
//        Logger logger = (Logger) LoggerFactory.getLogger(PlayersService.class);
//        listAppender = new ListAppender<>();
//        listAppender.start();
//        logger.addAppender(listAppender);
//    }
//
//    @Test
//    void registerPlayer_Success_LogsCorrectMessage() {
//        // Arrange
//        when(playersRepository.existsByEmail(playersModel.getEmail())).thenReturn(false);
//        when(playersRepository.existsByUsername(playersModel.getUsername())).thenReturn(false);
//        when(modelMapper.map(playersModel, PlayersDao.class)).thenReturn(playersDao);
//        when(playersRepository.save(playersDao)).thenReturn(playersDao);
//        when(modelMapper.map(playersDao, PlayersModel.class)).thenReturn(playersModel);
//
//        // Act
//        PlayersModel result = playersService.registerPlayer(playersModel);
//
//        // Assert
//        List<ILoggingEvent> logsList = listAppender.list;
//        assertEquals(1, logsList.size(), "Expected one log message");
//        assertEquals("INFO", logsList.get(0).getLevel().toString());
//        assertTrue(logsList.get(0).getFormattedMessage().contains("Player registered successfully: " + playersModel.getPlayerId()));
//    }
//
//    @Test
//    void login_ValidCredentials_LogsSuccessMessage() {
//        // Arrange
//        when(playersRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(playersDao));
//
//        // Act
//        //boolean result = playersService.login("john.doe@example.com", "Password@123");
//
//        // Assert
//       // assertTrue(result);
//        List<ILoggingEvent> logsList = listAppender.list;
//        assertEquals(1, logsList.size(), "Expected one log message");
//        assertEquals("INFO", logsList.get(0).getLevel().toString());
//        assertTrue(logsList.get(0).getFormattedMessage().contains("Successful login for email: john.doe@example.com"));
//    }
//
//    @Test
//    void login_InvalidCredentials_LogsFailureMessage() {
//        // Arrange
//        when(playersRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(playersDao));
//
//        // Act
//       // boolean result = playersService.login("john.doe@example.com", "WrongPassword");
//
//        // Assert
//        //assertFalse(result);
//        List<ILoggingEvent> logsList = listAppender.list;
//        assertEquals(1, logsList.size(), "Expected one log message");
//        assertEquals("WARN", logsList.get(0).getLevel().toString());
//        assertTrue(logsList.get(0).getFormattedMessage().contains("Failed login attempt for email: john.doe@example.com"));
//    }
//
//    @Test
//    void login_NonExistentEmail_LogsWarningMessage() {
//        // Arrange
//        when(playersRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());
//
//        // Act
//       // boolean result = playersService.login("unknown@example.com", "Password@123");
//
//        // Assert
//        //assertFalse(result);
//        List<ILoggingEvent> logsList = listAppender.list;
//        assertEquals(1, logsList.size(), "Expected one log message");
//        assertEquals("WARN", logsList.get(0).getLevel().toString());
//        assertTrue(logsList.get(0).getFormattedMessage().contains("No player found with email: unknown@example.com"));
//    }
//}