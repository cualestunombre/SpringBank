package seok.springBank.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import seok.springBank.config.argumentresolver.Login;
import seok.springBank.domain.account.Account;
import seok.springBank.domain.account.CheckingAccount;
import seok.springBank.domain.etcForms.DepositForm;
import seok.springBank.domain.member.Member;
import seok.springBank.repository.accountRepository.AccountRepositoryV2;
import seok.springBank.service.AccountService;
import seok.springBank.service.DepositService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class DepositController {
    private final AccountService accountService;
    private final AccountRepositoryV2 accountRepository;

    private final DepositService depositService;

    //입금 페이지 렌더링
    @GetMapping("/deposit")
    public String getDeposit(Model model, @Login Member loginMember){
        List<CheckingAccount> myCheckingAccount = accountService.getCheckingAccounts(loginMember.getId());
        model.addAttribute("accounts",myCheckingAccount);
        model.addAttribute("loginMember",loginMember);
        return "deposit";
    }
    //상세 입금 페이지 렌더링

    @GetMapping("/deposit/{id}")
    public String getDepositForm(HttpServletRequest req, HttpServletResponse res, Model model, @Login Member loginMember,
                                 @PathVariable("id")Long id){
        Account account = accountRepository.isMyAccount(loginMember.getId(),id);
        if(account == null){
            throw new IllegalArgumentException("Invalid Access");
        }
        else{
            model.addAttribute("loginMember",loginMember);
            model.addAttribute("account",account);
            return "depositForm";
        }
    }
    //입금 로직

    @PostMapping("/deposit")
    @ResponseBody
    public String handleDeposit(Model model, @Login Member loginMember, @Validated  @RequestBody DepositForm depositForm){
        depositService.depositMoney(loginMember.getId(), depositForm.getAccountNumber(), depositForm.getBalance());
        return "ok";

    }



}
