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
import org.springframework.web.bind.annotation.RequestParam;
import seok.springBank.config.argumentresolver.Login;
import seok.springBank.domain.account.Account;
import seok.springBank.domain.account.AccountSaveForm;
import seok.springBank.domain.account.CheckingAccount;
import seok.springBank.domain.account.CommodityAccount;
import seok.springBank.domain.member.Member;
import seok.springBank.exceptions.account.AccountMoreThanFive;
import seok.springBank.service.AccountService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URLDecoder;
import java.net.URLEncoder;

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
    @GetMapping("/account/created")
    public String createdAccount(@Login Member loginMember, Model model, @RequestParam("type") String type, @RequestParam("name")String name
    ,@RequestParam("number")String number){
        name = URLDecoder.decode(name);
        Account account;
        accountService.isValidCreatedAccount(type,name,number,loginMember.getId());

        if (type=="CHECKING_ACCOUNT"){
            account = new CheckingAccount();
        }
        else{
            account = new CommodityAccount();
        }

        account.setAccountNumber(number);
        account.setName(name);
        model.addAttribute("account",account);
        model.addAttribute("type",type);
        return "accountCreated";
    }
    @PostMapping("/account")
    public String createAccount(@Validated @ModelAttribute AccountSaveForm accountSaveForm, BindingResult bindingResult,HttpServletRequest req, HttpServletResponse res, @Login Member loginMember
    , Model model){
        //ModelAttribute정의
        model.addAttribute("loginMember",loginMember);
        String type = "";

        setPolicy(accountSaveForm);

        if(bindingResult.hasErrors()){
            // 필드에러가 발생하면 폼 다시 전송
            model.addAttribute("moreThanFive",false);
            return "accountOpening";
        }

        try{
            Account account = accountService.makeAccount(accountSaveForm,loginMember.getId());
            if(account instanceof CheckingAccount){
                type = "CHECKING_ACCOUNT";

            }
            else{
               type = "COMMODITY_ACCOUNT";
            }
            return "redirect:/account/created"+"?type="+type+"&name="+ URLEncoder.encode(account.getName())+"&number="+account.getAccountNumber();

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
