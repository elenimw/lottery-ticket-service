package com.habesha.lottery_ticket_service.service;

import com.habesha.lottery_ticket_service.dao.PlayersDao;
import com.habesha.lottery_ticket_service.dao.TicketDao;
import com.habesha.lottery_ticket_service.model.TicketModel;
import com.habesha.lottery_ticket_service.exception.LotteryException;
import com.habesha.lottery_ticket_service.repository.PlayersRepository;
import com.habesha.lottery_ticket_service.repository.TicketRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TicketService {
    private static final Logger logger = LoggerFactory.getLogger(TicketService.class);
    private static final double TICKET_COST = 2.0;
    private static final int MAX_NUMBERS = 6;
    private static final int MAX_NUMBER_VALUE = 100;

    @Autowired
    private PlayersRepository playerRepo;

    @Autowired
    private TicketRepository ticketRepo;

    @Autowired
    private ModelMapper modelMapper;

    public TicketService() {
        // Configure ModelMapper in constructor
        modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<TicketModel, TicketDao>() {
            @Override
            protected void configure() {
                map().setPlayerId(source.getPlayer_id());
                map().setDrawId(source.getDraw_id());
            }
        });
        modelMapper.addMappings(new PropertyMap<TicketDao, TicketModel>() {
            @Override
            protected void configure() {
                map().setPlayer_id(source.getPlayerId());
                map().setDraw_id(source.getDrawId());
            }
        });
    }

    @Transactional
    public TicketModel purchaseTicket(TicketModel request) {
        // Get authenticated player
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        PlayersDao player = playerRepo.findByEmail(email)
                .orElseThrow(() -> new LotteryException("Player not found"));

        // Validate drawId
        if (request.getDraw_id() == null) {
            throw new LotteryException("Draw ID is required");
        }

        // Validate numbers
        validateNumbers(request.getNumbers());

        // Check balance
        if (player.getBalance().doubleValue() < TICKET_COST) {
            throw new LotteryException("Insufficient balance");
        }

        // Deduct balance
        player.setBalance(player.getBalance().subtract(BigDecimal.valueOf(TICKET_COST)));
        playerRepo.save(player);

        // Create ticket
        TicketDao ticket = new TicketDao();
        ticket.setPlayerId(player.getPlayerId());
        ticket.setDrawId(request.getDraw_id());
        ticket.setNumbers(request.getNumbers());
        ticket.initializeFields(TICKET_COST);

        // Save ticket
        TicketDao saved = ticketRepo.save(ticket);
        logger.info("Ticket purchased: ticketId={}, playerId={}, drawId={}",
                    saved.getTicketId(), saved.getPlayerId(), saved.getDrawId());

        return modelMapper.map(saved, TicketModel.class);
    }

    public List<TicketModel> getTicketsByPlayer() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        PlayersDao player = playerRepo.findByEmail(email)
                .orElseThrow(() -> new LotteryException("Player not found"));

        List<TicketDao> tickets = ticketRepo.findByPlayerId(player.getPlayerId());
        logger.info("Retrieved {} tickets for playerId={}", tickets.size(), player.getPlayerId());

        return tickets.stream()
                .map(ticket -> modelMapper.map(ticket, TicketModel.class))
                .collect(Collectors.toList());
    }

    public List<TicketModel> getTicketsByDraw(UUID drawId) {
        if (drawId == null) {
            throw new LotteryException("Draw ID is required");
        }

        List<TicketDao> tickets = ticketRepo.findByDrawId(drawId);
        logger.info("Retrieved {} tickets for drawId={}", tickets.size(), drawId);

        return tickets.stream()
                .map(ticket -> modelMapper.map(ticket, TicketModel.class))
                .collect(Collectors.toList());
    }

    private void validateNumbers(List<Integer> numbers) {
        if (numbers == null || numbers.size() != MAX_NUMBERS) {
            throw new LotteryException("Exactly " + MAX_NUMBERS + " numbers are required");
        }
        for (Integer number : numbers) {
            if (number == null || number < 1 || number > MAX_NUMBER_VALUE) {
                throw new LotteryException("Numbers must be between 1 and " + MAX_NUMBER_VALUE);
            }
        }
        if (numbers.stream().distinct().count() != numbers.size()) {
            throw new LotteryException("Numbers must be unique");
        }
    }
}