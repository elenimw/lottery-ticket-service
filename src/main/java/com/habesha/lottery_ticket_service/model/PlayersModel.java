package com.habesha.lottery_ticket_service.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PlayersModel {

        private String username;
         private String   email;
         private BigDecimal balance;


}
