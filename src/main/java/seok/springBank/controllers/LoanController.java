package seok.springBank.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import seok.springBank.config.argumentresolver.Login;
import seok.springBank.domain.account.Account;
import seok.springBank.domain.etcForms.SimpleJsonResponse;
import seok.springBank.domain.member.Member;
import seok.springBank.domain.policy.LoanPolicy;
import seok.springBank.domain.policy.Policy;
import seok.springBank.service.AccountService;
import seok.springBank.service.PolicyService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class LoanController {
    private final PolicyService policyService;

    private final AccountService accountService;


    @GetMapping("/loan")
    public String getLoan(HttpServletRequest req, HttpServletResponse res, Model model , @Login Member loginMember){
        model.addAttribute("loginMember",loginMember);
        return "loan";
    }

    @GetMapping("/loan/create")
    public String chooseLoan(HttpServletRequest req,HttpServletResponse res,Model model, @Login Member loginMember){
        model.addAttribute("loginMember",loginMember);
        return "loanChoose";
    }

    @GetMapping("/loan/create/{type}")
    public String createLoan(@PathVariable("type") String type , HttpServletRequest req, HttpServletResponse res, Model model, @Login Member loginMember){
        Policy policy = policyService.findPolicyByName(type);
        if (!(policy instanceof LoanPolicy)){
            throw new IllegalArgumentException("Invalid Access");
        }
        LoanPolicy loanPolicy =  (LoanPolicy) policy;
        String title = TypeInterpreter.translate("title",type);



        model.addAttribute("loginMember",loginMember);
        model.addAttribute("title",title);
        model.addAttribute("maxAmount",loanPolicy.getMaxAmount());
        model.addAttribute("interestRate",loanPolicy.getInterestRate());
        model.addAttribute("maxDuration",loanPolicy.getMaxDuration());
        model.addAttribute("policyId",loanPolicy.getId());

        return "loanOpening";
    }
    @GetMapping("/loan/type/{id}")
    @ResponseBody
    public SimpleJsonResponse checkHavingLoanAlready(HttpServletRequest req, HttpServletResponse res,@PathVariable Long id){

        SimpleJsonResponse ret;
        if(id == null) {throw new IllegalArgumentException("Invalid Access");}
        List<Account> accounts = accountService.hasAccountByPolicyId(id);
        if(accounts.size() != 0) {
            ret = new SimpleJsonResponse("fail",200);
        }
        else{
            ret = new SimpleJsonResponse("success",200);
        }
        return ret;
    }

}

class TypeInterpreter{
    public static String translate(String target, String type){
        if (type.equals("worker")){
            if (target.equals("title")) return "직장인 안심 신용대출";
        }
        if (type.equals("house")){
            if (target.equals("title")) return "주택 담보 대출";
        }
        if (type.equals("business")){
            if (target.equals("title")) return "든든 자영업자 대출";
        }

        throw new IllegalArgumentException("Invalid Access");
    }

}
