package seok.springBank.controllers.accountControllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import seok.springBank.config.argumentresolver.Login;
import seok.springBank.domain.account.AccountCheckForm;
import seok.springBank.domain.account.AccountNumberForm;
import seok.springBank.domain.account.CheckingAccount;
import seok.springBank.domain.member.Member;
import seok.springBank.service.AccountService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@RequiredArgsConstructor
@Slf4j
@RestController
public class AccountJsonController {
    private final AccountService accountService;

    // 현재 계좌를 5개 초과해서 보유한 지 검증
    @PostMapping("/account/check")
    public AccountCheckForm checkAccount(@RequestBody AccountNumberForm accountNumber){
        CheckingAccount checkingAccount = accountService.getCheckingAccountByAccountNumber(accountNumber.getTargetAccountNumber(), accountNumber.getMyAccountNumber());
        return AccountCheckForm.makeAccountCheckForm(checkingAccount);
    }
}
