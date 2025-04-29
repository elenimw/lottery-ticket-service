package com.habesha.lottery_ticket_service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class LotteryException extends RuntimeException {

    private HttpStatus status;

    public LotteryException(String message) {
        super(message);
    }

    public LotteryException(String message, Throwable cause) {
        super(message, cause);
    }

  public LotteryException(String message, HttpStatus status){
        super(message);
        this.status=status;
  }
}