package seok.springBank.exceptions.account;

public class NotACheckingAccountException extends RuntimeException{
    public NotACheckingAccountException(String message){
        super(message);
    }
    public NotACheckingAccountException(String message,Throwable r){
        super(message,r);
    }
    public NotACheckingAccountException(){

    }
}
