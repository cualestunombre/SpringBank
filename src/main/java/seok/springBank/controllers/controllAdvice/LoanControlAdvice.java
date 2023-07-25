package seok.springBank.controllers.controllAdvice;

import org.hibernate.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import seok.springBank.controllers.LoanController;
import seok.springBank.controllers.TransferController;
import seok.springBank.domain.etcForms.ErrorResult;
import seok.springBank.exceptions.account.AlreadyHasThisLoan;
import seok.springBank.exceptions.account.BalanceNotEnoughException;

@RestControllerAdvice(assignableTypes = {LoanController.class})
public class LoanControlAdvice {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AlreadyHasThisLoan.class)
    @RequestMapping(headers = "Accept=application/json")
    public ErrorResult alreadyHasThisLoan(AlreadyHasThisLoan e){
        return new ErrorResult(e.getMessage(),400);
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @RequestMapping(headers = "Accept=application/json")
    public ErrorResult typeMismatch(HttpMessageNotReadableException e){
        return new ErrorResult("올바른 정보가 입력되었는지 확인해보세요",400);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @RequestMapping(headers = "Accept=application/json")
    public ErrorResult nullCheck(IllegalArgumentException e){
        return new ErrorResult(e.getMessage(),400);
    }

}
