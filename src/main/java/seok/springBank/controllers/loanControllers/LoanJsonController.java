package seok.springBank.controllers.loanControllers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import seok.springBank.config.argumentresolver.Login;
import seok.springBank.domain.account.Account;
import seok.springBank.domain.account.LoanAccount;
import seok.springBank.domain.account.LoanAccountSaveForm;
import seok.springBank.domain.account.LoanTransferForm;
import seok.springBank.domain.etcForms.InformationDto;
import seok.springBank.domain.etcForms.OverdueDto;
import seok.springBank.domain.etcForms.SimpleJsonResponse;
import seok.springBank.domain.member.Member;
import seok.springBank.domain.policy.LoanPolicy;
import seok.springBank.domain.transactions.LoanTransactions;
import seok.springBank.domain.transactions.TransactionDto;
import seok.springBank.exceptions.account.AlreadyHasThisLoan;
import seok.springBank.exceptions.account.HasLatePayment;
import seok.springBank.exceptions.account.HasNoCheckingAccount;
import seok.springBank.service.AccountService;
import seok.springBank.service.MemberService;
import seok.springBank.service.TransactionsService;
import seok.springBank.service.TransferService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class LoanJsonController{

    private final TransactionsService transactionsService;
    private final AccountService accountService;
    private final MemberService memberService;

    private  final TransferService transferService;

    @ResponseBody
    @GetMapping("/loan/inquiry/information")
    public InformationDto getInformation(@RequestParam(value = "accountNumber",required = true)String accountNumber,
                                         @Login Member loginMember){

        accountService.isMyAccount(accountNumber,loginMember.getId());
        LoanAccount loanAccount = accountService.getLoanAccountByAccountNumber(accountNumber);
        LoanPolicy loanPolicy = (LoanPolicy) loanAccount.getPolicy();
        Long amount = (loanAccount.getLeftCount()
                *Math.round(loanAccount.getAmount()*loanPolicy.getInterestRate()*0.01))
                +loanAccount.getOverdueAmount()
                +loanAccount.getAmount();
        if(loanAccount.getStatus().equals("상환완료")){
            amount = 0L;
        }
        return new InformationDto(loanAccount.getAmount(),amount,loanAccount.getLeftCount()+loanAccount.getOverdueCnt());

    }
    // 계좌 연체 정보 조회 로직
    @ResponseBody
    @GetMapping("/loan/overdue")
    public OverdueDto getOverDue(@RequestParam(value = "accountNumber",required = true)String accountNumber,
                                 @Login Member loginMember){
        accountService.isMyAccount(accountNumber,loginMember.getId());
        LoanAccount loanAccount = accountService.getLoanAccountByAccountNumber(accountNumber);
        return new OverdueDto(loanAccount.getOverdueAmount(),loanAccount.getOverdueCnt());
    }

    // 대출계좌 출금 로직
    @ResponseBody
    @PostMapping("/loan/withdraw")
    public SimpleJsonResponse withdrawLoan(@Validated @RequestBody LoanTransferForm loanTransferForm,
                                           BindingResult bindingResult, @Login Member loginMember){
        if(bindingResult.hasErrors()){
            throw new IllegalArgumentException("Invalid Access");
        }
        transferService.transferToChecking(loanTransferForm,loginMember);
        return new SimpleJsonResponse("success",200);

    }
    // 대출계좌 이체 로직
    @ResponseBody
    @PostMapping("/loan/transfer")
    public SimpleJsonResponse transferToLoan(@Validated @RequestBody LoanTransferForm loanTransferForm
        ,BindingResult bindingResult,@Login Member loginMember){
        if(bindingResult.hasErrors()){
            throw new IllegalArgumentException("Invalid Access");
        }
        transferService.transferToLoan(loanTransferForm,loginMember);
        return new SimpleJsonResponse("success",200);

    }

    // 대출계좌 생성 로직
    @ResponseBody
    @PostMapping("/loan/create")
    public SimpleJsonResponse createLoan(@Validated @RequestBody LoanAccountSaveForm saveForm,
                                         BindingResult bindingResult, @Login Member member){
        if (bindingResult.hasErrors()){
            throw new IllegalArgumentException("정보를 올바르게 입력하셨는 지 확인해보세요");
        }
        LoanAccount account = accountService.createLoan(saveForm,member);
        return new SimpleJsonResponse(account.getAccountNumber(),200);
    }

    //특정 정책에 대해서 계좌 생성이 가능한 지 판별하는 로직
    @GetMapping("/loan/type/{id}")
    @ResponseBody
    public SimpleJsonResponse checkHavingLoanAlready(@PathVariable Long id,@Login Member loginMember){

        SimpleJsonResponse ret;
        if(id == null) {throw new IllegalArgumentException("비정상적인 접근입니다");}
        List<Account> accounts = accountService.hasAccountByPolicyId(id);

        // 이미 생성된 계좌가 있다면
        if(accounts.size() != 0) {
            throw new AlreadyHasThisLoan("alreadyHas");
        }
        // 고객이 입출금 계좌가 없다면
        else if(!accountService.hasCheckingAccount(loginMember)){
            throw new HasNoCheckingAccount("noChecking");
        }
        // 고객이 연체가 있다면
        else if(memberService.checkLatePayment(loginMember)){
            throw new HasLatePayment("late");
        }
        else{
            ret = new SimpleJsonResponse("success",200);
        }
        return ret;
    }
    @GetMapping("/loan/inquiry/count")
    @ResponseBody
    public SimpleJsonResponse loanTransactionCount(@Login Member loginMember,@RequestParam(required = true,value = "accountNumber")String accountNumber){
        accountService.isMyAccount(accountNumber,loginMember.getId());
        Account account = accountService.getLoanAccountByAccountNumber(accountNumber);
        return new SimpleJsonResponse(transactionsService.getTransactionsPage(account.getId()).toString(),200);
    }

    @GetMapping("/loan/inquiry/count/{id}/{page}")
    @ResponseBody
    public List<TransactionDto> loanTransactionPage(@Login Member loginMember, @PathVariable(required = true) Long id,@PathVariable(required = true)Long page){
        List<TransactionDto> transactionDtos = transactionsService.getTransactions(id,page,loginMember).stream().peek((e)->{

        }).collect(Collectors.toList());
        System.out.println(transactionDtos.size());
        return transactionDtos;
    }



}