package seok.springBank.exceptions.account;

public class HasNoCheckingAccount extends RuntimeException{
    public HasNoCheckingAccount(String message){
        super(message);
    }
    public HasNoCheckingAccount(String message, Throwable r){
        super(message,r);
    }
    public HasNoCheckingAccount(){}

}
