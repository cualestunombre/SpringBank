package seok.springBank.exceptions.account;

public class MyAccountException extends RuntimeException {
    public MyAccountException(String message){
        super(message);
    }
    public MyAccountException(String message, Throwable r){
        super(message,r);
    }
    public MyAccountException(){

    }
}
