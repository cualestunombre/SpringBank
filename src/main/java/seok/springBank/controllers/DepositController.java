package seok.springBank.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import seok.springBank.config.argumentresolver.Login;
import seok.springBank.domain.account.Account;
import seok.springBank.domain.account.CheckingAccount;
import seok.springBank.domain.member.Member;
import seok.springBank.repository.accountRepository.AccountRepositoryV2;
import seok.springBank.service.AccountService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class DepositController {
    private final AccountService accountService;
    private final AccountRepositoryV2 accountRepository;

    @GetMapping("/deposit")
    public String getDeposit(HttpServletRequest req, HttpServletResponse res, Model model, @Login Member loginMember){
        List<CheckingAccount> myCheckingAccount = accountService.getCheckingAccounts(loginMember.getId());
        model.addAttribute("accounts",myCheckingAccount);
        model.addAttribute("loginMember",loginMember);
        return "deposit";
    }

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

}
