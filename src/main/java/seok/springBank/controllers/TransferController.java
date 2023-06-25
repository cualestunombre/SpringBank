package seok.springBank.controllers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import seok.springBank.config.argumentresolver.Login;
import seok.springBank.domain.account.Account;
import seok.springBank.domain.account.CheckingAccount;
import seok.springBank.domain.etcForms.ErrorResult;
import seok.springBank.domain.etcForms.SimpleJsonResponse;
import seok.springBank.domain.member.Member;
import seok.springBank.domain.transfer.TransferForm;
import seok.springBank.repository.accountRepository.AccountRepositoryV2;
import seok.springBank.service.AccountService;
import seok.springBank.service.TransferService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class TransferController {
    private final AccountService accountService;
    private final AccountRepositoryV2 accountRepositoryV2;

    private final TransferService transferService;

    @GetMapping("/transfer")
    public String getTransfer(HttpServletRequest req, HttpServletResponse res, Model model, @Login Member loginMember){
        List<CheckingAccount> myCheckingAccount = accountService.getCheckingAccounts(loginMember.getId());
        model.addAttribute("accounts",myCheckingAccount);
        model.addAttribute("loginMember",loginMember);
        return "transfer";
    }
    @GetMapping("/transfer/{id}")
    public String getSelectedTransfer(HttpServletRequest req,HttpServletResponse res, Model model,@Login Member loginMember
    ,@PathVariable Long id){
        if (accountRepositoryV2.isMyAccount(loginMember.getId(),id)==null){
            throw new IllegalArgumentException("Invalid Access");
        }
        CheckingAccount checkingAccount = accountService.getCheckingAccountById(id);
        model.addAttribute("account",checkingAccount);
        model.addAttribute("loginMember",loginMember);
        return "transferForm";
    }
    @PostMapping("/transfer")
    @ResponseBody
    public SimpleJsonResponse handleTransfer(HttpServletRequest req, HttpServletResponse res, @Login Member loginMember, @Validated  @RequestBody TransferForm transferForm,
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
