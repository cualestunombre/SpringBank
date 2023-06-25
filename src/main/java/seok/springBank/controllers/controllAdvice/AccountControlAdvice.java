package seok.springBank.controllers.controllAdvice;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import seok.springBank.controllers.AccountController;
import seok.springBank.domain.etcForms.ErrorResult;
import seok.springBank.exceptions.account.MyAccountException;
import seok.springBank.exceptions.account.NotACheckingAccountException;

import java.util.NoSuchElementException;

@RestControllerAdvice(assignableTypes = {AccountController.class})
public class AccountControlAdvice {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    @RequestMapping(headers = "Accept=application/json")
    public ErrorResult notFoundHandle(NoSuchElementException e){
        return new ErrorResult(e.getMessage(),404);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MyAccountException.class)
    @RequestMapping(headers = "Accept=application/json")
    public ErrorResult myAccountFail(MyAccountException e){
        return new ErrorResult(e.getMessage(),400);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotACheckingAccountException.class)
    @RequestMapping(headers = "Accept=application/json")
    public ErrorResult notACheckingAccount(NotACheckingAccountException e){
        return new ErrorResult(e.getMessage(),401);
    }
}
