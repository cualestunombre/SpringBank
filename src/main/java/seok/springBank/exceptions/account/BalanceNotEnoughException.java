package seok.springBank.exceptions.account;

public class BalanceNotEnoughException extends RuntimeException {
    public BalanceNotEnoughException(String message){
        super(message);
    }
    public BalanceNotEnoughException(String message, Throwable r){
        super(message,r);
    }
    public BalanceNotEnoughException() {

    }
}
