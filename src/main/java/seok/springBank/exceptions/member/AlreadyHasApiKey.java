package seok.springBank.exceptions.member;

public class AlreadyHasApiKey extends RuntimeException{
    public AlreadyHasApiKey (String message){
        super(message);
    }
    public AlreadyHasApiKey (String message,Throwable r){
        super(message,r);
    }
    public AlreadyHasApiKey (){

    }
}
