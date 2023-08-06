package seok.springBank.controllers.loanControllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import seok.springBank.config.argumentresolver.Login;
import seok.springBank.domain.account.*;
import seok.springBank.domain.member.Member;
import seok.springBank.domain.policy.LoanPolicy;
import seok.springBank.domain.policy.Policy;
import seok.springBank.service.AccountService;
import seok.springBank.service.PolicyService;
import seok.springBank.utility.LoanTypeInterpreter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class LoanController {
    private final PolicyService policyService;

    private final AccountService accountService;

    // 대출계좌 이체 페이지 렌더링
    @GetMapping("/loan/transfer")
    public String loanTransfer(Model model, @Login Member loginMember){
        model.addAttribute("loginMember",loginMember);
        List<LoanAccount> loanAccounts = accountService.getLoanAccounts(loginMember);
        List<CheckingAccount> checkingAccounts = accountService.getCheckingAccounts(loginMember.getId());
        model.addAttribute("loanAccounts",loanAccounts);
        model.addAttribute("checkingAccounts",checkingAccounts);
        return "loanTransfer";
    }


    // 대출서비스 첫 페이지 렌더링
    @GetMapping("/loan")
    public String getLoan(Model model , @Login Member loginMember){
        model.addAttribute("loginMember",loginMember);
        return "loan";
    }

    // 대출 생성 페이지 렌더링
    @GetMapping("/loan/create")
    public String chooseLoan(Model model, @Login Member loginMember){
        model.addAttribute("loginMember",loginMember);
        return "loanChoose";
    }

    //3가지 타입의 대출 생성 페이지 렌더링
    @GetMapping("/loan/create/{type}")
    public String createLoan(@PathVariable("type") String type ,Model model, @Login Member loginMember){
        Policy policy = policyService.findPolicyByName(type);

        if (!(policy instanceof LoanPolicy)){
            throw new IllegalArgumentException("Invalid Access");
        }

        LoanPolicy loanPolicy =  (LoanPolicy) policy;
        String title = LoanTypeInterpreter.translate("title",type);



        model.addAttribute("loginMember",loginMember);
        model.addAttribute("title",title);
        model.addAttribute("maxAmount",loanPolicy.getMaxAmount());
        model.addAttribute("interestRate",loanPolicy.getInterestRate());
        model.addAttribute("maxDuration",loanPolicy.getMaxDuration());
        model.addAttribute("policyId",loanPolicy.getId());

        return "loanOpening";
    }


    // 대출 계좌 생성 페이지 렌더링
    @GetMapping("/loan/created")
    public String createdLoan(@Login Member loginMember, Model model, @RequestParam("name")String name
            ,@RequestParam("number")String number){
        name = URLDecoder.decode(name);
        LoanAccount account = accountService.getLoanAccountByAccountNumber(number);
        accountService.isValidCreatedAccount("LOAN_ACCOUNT",account.getName(),number,loginMember.getId());
        model.addAttribute("name",name);
        model.addAttribute("accountNumber",number);
        model.addAttribute("interestRate",account.getPolicy().getInterestRate());
        model.addAttribute("leftCount",account.getLeftCount());
        return "loanCreated";
    }

    //대출계좌 출금 페이지 렌더링
    @GetMapping("/loan/withdraw")
    public String loanWithdraw(Model model,@Login Member loginMember){
        // made sure that expired accounts would never show up!!
        List<LoanAccount> loanAccounts = accountService.getLoanAccounts(loginMember);
        List<CheckingAccount> checkingAccounts = accountService.getCheckingAccounts(loginMember.getId());
        model.addAttribute("loanAccounts",loanAccounts);
        model.addAttribute("checkingAccounts",checkingAccounts);
        model.addAttribute("loginMember",loginMember);
        return "loanWithdraw";
    }
    // 대출계좌 조회 페이지 렌더링
    @GetMapping("/loan/inquiry")
    public String getLoanInquiry(Model model, @Login Member loginMember){
        List<LoanAccount> loanAccounts = accountService.getLoanAccounts(loginMember);
        model.addAttribute("loanAccounts",loanAccounts);
        model.addAttribute("loginMember",loginMember);
        return "loanInquiry";
    }
    // 특정된 대출계좌 조회 페이지 렌더링
    @GetMapping("/loan/inquiry/{id}")
    public String loanInquiry(Model model,@Login Member loginMember,@PathVariable(required = true) Long id){
        LoanAccount loanAccount = accountService.getAccountsById(id)
                .filter((e)->e instanceof LoanAccount)
                .filter((e)->e.getMember().getId() == loginMember.getId())
                .map(e->((LoanAccount)e))
                .stream().findFirst()
                .orElseThrow(()->{throw new IllegalArgumentException("Invalid Access");});
        model.addAttribute("loginMember",loginMember);
        model.addAttribute("account",loanAccount);
        return "loanInquiryDetail";
    }

}



