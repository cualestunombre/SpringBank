package seok.springBank.controllers.controllAdvice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import seok.springBank.controllers.AccountController;
import seok.springBank.controllers.TransferController;
import seok.springBank.domain.etcForms.ErrorResult;
import seok.springBank.exceptions.account.BalanceNotEnoughException;
import seok.springBank.exceptions.account.MyAccountException;

@RestControllerAdvice(assignableTypes = {TransferController.class})
public class TransferControlAdvice {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BalanceNotEnoughException.class)
    @RequestMapping(headers = "Accept=application/json")
    public ErrorResult myAccountFail(BalanceNotEnoughException e){
        return new ErrorResult(e.getMessage(),401);
    }
}
