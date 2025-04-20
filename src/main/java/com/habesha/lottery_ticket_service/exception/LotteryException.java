package com.habesha.lottery_ticket_service.exception;

public class LotteryException extends RuntimeException {
    public LotteryException(String message) {
        super(message);
    }

    public LotteryException(String message, Throwable cause) {
        super(message, cause);
    }
}