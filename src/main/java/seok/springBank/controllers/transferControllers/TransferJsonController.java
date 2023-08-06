package seok.springBank.controllers.transferControllers;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import seok.springBank.config.argumentresolver.Login;
import seok.springBank.domain.etcForms.SimpleJsonResponse;
import seok.springBank.domain.member.Member;
import seok.springBank.domain.transfer.TransferForm;
import seok.springBank.service.AccountService;
import seok.springBank.service.TransferService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
public class TransferJsonController {
    private final AccountService accountService;
    private final TransferService transferService;
    //송금 로직
    @PostMapping("/transfer")
    @ResponseBody
    public SimpleJsonResponse handleTransfer(HttpServletResponse res, @Login Member loginMember, @Validated @RequestBody TransferForm transferForm,
                                             BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            res.setStatus(400);
            return new SimpleJsonResponse("Invalid Access",400);
        }

        accountService.isMyAccount(transferForm.getMyAccountNumber(),loginMember.getId());
        accountService.isCheckingAccount(transferForm.getMyAccountNumber());
        accountService.isCheckingAccount(transferForm.getTargetAccountNumber());
        transferService.sendMoneyChecking(transferForm.getMyAccountNumber(), transferForm.getTargetAccountNumber(),transferForm.getAmount());

        return new SimpleJsonResponse("Success",200);

    }
}
