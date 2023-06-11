package seok.springBank.controllers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import seok.springBank.config.argumentresolver.Login;
import seok.springBank.domain.account.Account;
import seok.springBank.domain.account.CheckingAccount;
import seok.springBank.domain.member.Member;
import seok.springBank.repository.accountRepository.AccountRepositoryV2;
import seok.springBank.service.AccountService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class TransferController {
    private final AccountService accountService;
    private final AccountRepositoryV2 accountRepositoryV2;

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
        return "transferForm";
    }

}
