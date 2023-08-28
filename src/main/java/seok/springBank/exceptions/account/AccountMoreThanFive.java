package seok.springBank.exceptions.account;

public class AccountMoreThanFive extends RuntimeException{
    public AccountMoreThanFive(String message){
        super(message);
    }
    public AccountMoreThanFive(String message,Throwable r){
        super(message,r);
    }
    public AccountMoreThanFive(){

    }
}
