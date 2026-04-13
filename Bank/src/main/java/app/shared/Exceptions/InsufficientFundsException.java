package app.shared.Exceptions;

import java.math.BigDecimal;

public class InsufficientFundsException extends BusinessException {

    public InsufficientFundsException(String accountNumber, BigDecimal available, BigDecimal requested) {
        super("Insufficient funds in account '" + accountNumber
                + "': available " + available + ", requested " + requested);
    }

    public InsufficientFundsException(String message) {
        super(message);
    }
}