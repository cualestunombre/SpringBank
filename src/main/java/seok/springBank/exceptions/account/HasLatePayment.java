package seok.springBank.exceptions.account;

public class HasLatePayment extends RuntimeException{
    public HasLatePayment(String message){
        super(message);
    }
    public HasLatePayment(String message, Throwable r){
        super(message,r);
    }
    public HasLatePayment(){}

}
