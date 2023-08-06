package seok.springBank.controllers.controllAdvice;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import seok.springBank.controllers.loanControllers.LoanJsonController;
import seok.springBank.domain.etcForms.ErrorResult;
import seok.springBank.exceptions.account.AlreadyHasThisLoan;

@RestControllerAdvice(assignableTypes = {LoanJsonController.class})
public class LoanControlAdvice {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AlreadyHasThisLoan.class)
    public ErrorResult alreadyHasThisLoan(AlreadyHasThisLoan e){
        e.printStackTrace();
        return new ErrorResult(e.getMessage(),400);
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResult typeMismatch(HttpMessageNotReadableException e){
        e.printStackTrace();
        return new ErrorResult("올바른 정보가 입력되었는지 확인해보세요",400);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResult nullCheck(IllegalArgumentException e){
        e.printStackTrace();
        return new ErrorResult(e.getMessage(),400);
    }

}
