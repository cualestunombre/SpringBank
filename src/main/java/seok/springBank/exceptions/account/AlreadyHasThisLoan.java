package seok.springBank.exceptions.account;

public class AlreadyHasThisLoan extends RuntimeException {
    public AlreadyHasThisLoan (String message){
        super(message);
    }
    public AlreadyHasThisLoan (String message, Throwable r){
        super(message,r);
    }
    public AlreadyHasThisLoan () {

    }
}