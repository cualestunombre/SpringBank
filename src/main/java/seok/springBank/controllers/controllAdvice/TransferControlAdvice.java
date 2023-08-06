package seok.springBank.controllers.controllAdvice;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import seok.springBank.controllers.transferControllers.TransferController;
import seok.springBank.controllers.transferControllers.TransferJsonController;
import seok.springBank.domain.etcForms.ErrorResult;
import seok.springBank.exceptions.account.BalanceNotEnoughException;

@RestControllerAdvice(assignableTypes = {TransferJsonController.class})
public class TransferControlAdvice {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BalanceNotEnoughException.class)
    public ErrorResult myAccountFail(BalanceNotEnoughException e){

        e.printStackTrace();
        return new ErrorResult(e.getMessage(),401);
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResult typeMismatch(HttpMessageNotReadableException e){
        e.printStackTrace();
        return new ErrorResult("올바른 정보가 입력되었는지 확인해보세요",400);
    }
}
