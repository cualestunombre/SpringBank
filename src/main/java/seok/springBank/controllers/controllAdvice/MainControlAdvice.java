package seok.springBank.controllers.controllAdvice;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import seok.springBank.controllers.loanControllers.LoanJsonController;
import seok.springBank.controllers.mainControllers.MainJsonController;
import seok.springBank.domain.etcForms.ErrorResult;
import seok.springBank.exceptions.account.AlreadyHasThisLoan;
import seok.springBank.exceptions.member.AlreadyHasApiKey;

@RestControllerAdvice(assignableTypes = {MainJsonController.class})
public class MainControlAdvice {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AlreadyHasApiKey.class)
    public ErrorResult alreadyHasApiKey(AlreadyHasApiKey e){
        e.printStackTrace();
        return new ErrorResult(e.getMessage(),400);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegal(IllegalArgumentException e){
        e.printStackTrace();
        return new ErrorResult(e.getMessage(),400);
    }



}
