package seok.springBank.controllers.controllAdvice;


import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import seok.springBank.controllers.accountControllers.AccountController;
import seok.springBank.controllers.accountControllers.AccountJsonController;
import seok.springBank.domain.etcForms.ErrorResult;
import seok.springBank.exceptions.account.MyAccountException;
import seok.springBank.exceptions.account.NotACheckingAccountException;

import java.util.NoSuchElementException;

@RestControllerAdvice(assignableTypes = {AccountJsonController.class})
public class AccountControlAdvice {
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResult typeMismatch(HttpMessageNotReadableException e){
        e.printStackTrace();
        return new ErrorResult("올바른 정보가 입력되었는지 확인해보세요",400);
    }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public ErrorResult notFoundHandle(NoSuchElementException e){
        e.printStackTrace();
        return new ErrorResult(e.getMessage(),404);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MyAccountException.class)
    public ErrorResult myAccountFail(MyAccountException e){

        e.printStackTrace();
        return new ErrorResult(e.getMessage(),400);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotACheckingAccountException.class)
    public ErrorResult notACheckingAccount(NotACheckingAccountException e){
        e.printStackTrace();
        return new ErrorResult(e.getMessage(),401);
    }
}
