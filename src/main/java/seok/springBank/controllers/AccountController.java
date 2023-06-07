package seok.springBank.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import seok.springBank.config.argumentresolver.Login;
import seok.springBank.domain.account.Account;
import seok.springBank.domain.account.AccountSaveForm;
import seok.springBank.domain.account.CheckingAccount;
import seok.springBank.domain.member.Member;
import seok.springBank.exceptions.account.AccountMoreThanFive;
import seok.springBank.service.AccountService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@Slf4j
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping("/account")
    public String getCreateAccount(HttpServletRequest req, HttpServletResponse res,@Login Member loginMember
    ,Model model) {
        model.addAttribute("accountSaveForm",new AccountSaveForm());
        model.addAttribute("loginMember",loginMember);
        model.addAttribute("moreThanFive",false);
        return "accountOpening";
    }

    @PostMapping("/account")
    public String createAccount(@Validated @ModelAttribute AccountSaveForm accountSaveForm, BindingResult bindingResult,HttpServletRequest req, HttpServletResponse res, @Login Member loginMember
    , Model model){
        //ModelAttribute정의
        model.addAttribute("loginMember",loginMember);

        setPolicy(accountSaveForm);

        if(bindingResult.hasErrors()){
            // 필드에러가 발생하면 폼 다시 전송
            model.addAttribute("moreThanFive",false);
            return "accountOpening";
        }

        try{
            Account account = accountService.makeAccount(accountSaveForm,loginMember.getId());
            model.addAttribute("account",account);
            if(account instanceof CheckingAccount){
                model.addAttribute("type","CHECKING_ACCOUNT");
            }
            else{
                model.addAttribute("type","COMMODITY_ACCOUNT");
            }
            return "accountCreated";

        }
        // 비지니스 에러 처리
        catch (AccountMoreThanFive ac){
            model.addAttribute("moreThanFive",true);
            return "accountOpening";
        }

    }

    //추후에 다른 방법 강구
    private void setPolicy(AccountSaveForm accountSaveForm){
        if (accountSaveForm.getDtype().equals("CHECKING_ACCOUNT")){
            accountSaveForm.setPolicyId(2L);
        }
        else{
            accountSaveForm.setPolicyId(3L);
        }

    }
}
