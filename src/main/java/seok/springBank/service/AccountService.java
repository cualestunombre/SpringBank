package seok.springBank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import seok.springBank.domain.account.*;
import seok.springBank.domain.member.Member;
import seok.springBank.domain.policy.CheckingPolicy;
import seok.springBank.domain.policy.CommodityPolicy;
import seok.springBank.domain.policy.LoanPolicy;
import seok.springBank.domain.policy.Policy;
import seok.springBank.exceptions.account.*;
import seok.springBank.repository.accountRepository.AccountRepositoryV2;
import seok.springBank.repository.memberRepository.MemberRepository;
import seok.springBank.repository.memberRepository.MemberRepositoryV2;
import seok.springBank.repository.policyRepository.PolicyRepositoryV2;
import seok.springBank.utility.AccountNumberGenerator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Transactional(isolation = Isolation.SERIALIZABLE)
@RequiredArgsConstructor
@Service
public class AccountService {
    private final AccountRepositoryV2 accountRepository;
    private final MemberRepositoryV2 memberRepository;
    private final PolicyRepositoryV2 policyRepository;

    public LoanAccount getLoanAccountByAccountNumber(String accountNumber){
        List<Account> accounts = accountRepository.findByAccountNumber(accountNumber);
        if (accounts.size() != 1){
            throw new IllegalArgumentException("Invalid Access");
        }
        Account account = accounts.get(0);
        if (!(account instanceof LoanAccount)){
            throw new IllegalArgumentException("Invalid Access");
        }
        return (LoanAccount) account;
    }
    public LoanAccount createLoan(LoanAccountSaveForm saveForm,Member member){
        Policy policy = policyRepository.findById(saveForm.getPolicyId()).orElseThrow(()->{throw new IllegalArgumentException("Invalid Access");});
        LoanPolicy loanPolicy;
        String accountNumber;


        if (!(policy instanceof LoanPolicy)){
            throw new IllegalArgumentException("Invalid Access");
        }
        else{
            loanPolicy = (LoanPolicy) policy;
        }

        //expire 되지 않은 계좌만 찾아 옴
        if (hasAccountByPolicyId(saveForm.getPolicyId()).size()!=0){
            throw new AlreadyHasThisLoan("Invalid Access");
        }

        if(loanPolicy.getMaxAmount() < saveForm.getAmount() || loanPolicy.getMaxDuration() < saveForm.getDuration() ){
            throw new IllegalArgumentException("Invalid Access");
        }
        while (true) {
            accountNumber = "11" + AccountNumberGenerator.generateRandomNumber();
            if (accountRepository.findByAccountNumber(accountNumber).isEmpty()) {
                break;
            }
        }

        LoanAccount loanAccount = new LoanAccount();
        loanAccount.setPolicy(policy);
        loanAccount.setAccountNumber(accountNumber);
        loanAccount.setExpired(false);
        loanAccount.setStatus("비연체");
        loanAccount.setName("대출계좌");
        loanAccount.setBalance(saveForm.getAmount());
        loanAccount.setLeftCount(saveForm.getDuration());
        loanAccount.setMember(member);
        loanAccount.setOverdueAmount(0L);
        loanAccount.setCreatedAt(LocalDateTime.now());
        loanAccount.setOverdueCnt(0L);

        accountRepository.save(loanAccount);
        return loanAccount;

    }

    public List<Account> hasAccountByPolicyId(Long id){ //expire되지 않은 계좌를 찾아 옴
        Policy policy = policyRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Invalid Access"));
        //만료되지 않은 특정 정책을 가진 계좌들을 불러 옴
        List<Account> account = accountRepository.findAccountByPolicyAndNotExpired(policy);
        return account;
    }


    public void isCheckingAccount(String accountNumber){
        List<Account> accounts = accountRepository.findByAccountNumber(accountNumber);
        if (accounts.size()==0) throw new IllegalArgumentException("Invalid Access");
        else{
            if (accounts.get(0) instanceof CheckingAccount) return;
            else{
                throw new IllegalArgumentException("Invalid Access");
            }
        }
    }
    public void isMoneyEnough(String accountNumber,Long amount){
        List<Account> accounts = accountRepository.findByAccountNumber(accountNumber);
        if (accounts.size()==0) throw new IllegalArgumentException("Invalid Access");
        else{
            if (accounts.get(0).getBalance()>=amount) return;
            else{
                throw new BalanceNotEnoughException("Balance is not enough");
            }
        }
    }
    public void isMyAccount(String accountNumber,Long memberId){
        Member findMember = memberRepository.findById(memberId).orElseThrow(()->new IllegalArgumentException("Invalid Access"));
        List<Account> accounts = accountRepository.findByAccountNumber(accountNumber);
        if (accounts.size()==0) throw new IllegalArgumentException("Invalid Access");
        else{
            if (findMember==accounts.get(0).getMember()) return ;
            else throw new IllegalArgumentException("Invalid Access");
        }
    }
    public void isValidCreatedAccount(String type, String name, String number,Long memberId){
        System.out.println(type);
        if (!StringUtils.hasText(type)|| !StringUtils.hasText(name) || !StringUtils.hasText(number)
        || !(type.equals("COMMODITY_ACCOUNT" )|| type.equals("CHECKING_ACCOUNT") || type.equals("LOAN_ACCOUNT"))  ){
            throw new IllegalArgumentException("Invalid Access");
        }

        Optional<Account> account = accountRepository.isValidCreatedAccount(memberId,name,number);
        account.orElseThrow(()->new IllegalArgumentException("Invalid Access"));

    }
    public CheckingAccount getCheckingAccountByAccountNumber(String targetAccountNumber,String myAccountNumber){
       List<Account> accounts = accountRepository.findByAccountNumber(targetAccountNumber);

       Account account=null;
       if (accounts.size()!=0){
           account = accounts.get(0);
       }
       if(account!=null && account instanceof CheckingAccount && !account.getAccountNumber().equals(myAccountNumber)){
           return (CheckingAccount) account;
       }
       else if(account!=null && account instanceof CheckingAccount && account.getAccountNumber().equals(myAccountNumber)){
           throw new MyAccountException("Can't transfer to your own account");
       }
       else if(account!=null && ! (account instanceof CheckingAccount)){
           throw new NotACheckingAccountException("Only Checking Account Available");
       }

       throw new NoSuchElementException("Not Found");
    }

    public CheckingAccount getCheckingAccountById(Long id){
        Account account = accountRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Invalid Access"));
        if (account instanceof CheckingAccount){
            return (CheckingAccount) account;
        }
        else{
            throw new IllegalArgumentException("Invalid Access");
        }
    }

    public List<CheckingAccount> getCheckingAccounts(Long memberId){
        return accountRepository.findCheckingAccountById(memberId);
    }

    public Account makeAccount(AccountSaveForm accountSaveForm, Long memberId) throws AccountMoreThanFive{

        Account account=null;
        Member member = memberRepository.findById(memberId).orElseThrow(()->new IllegalArgumentException("Invalid Access"));
        Policy policy = policyRepository.findById(accountSaveForm.getPolicyId()).orElseThrow(()->new IllegalArgumentException("Invalid Access"));
        String dtype = accountSaveForm.getDtype();
        String accountNumber;
        String name = accountSaveForm.getName();


       /*
        적금계좌 (00), 입출금계좌(01), 상품계좌(10), 대출계좌(11)
         */

        if (dtype.equals("CHECKING_ACCOUNT")) {
            if (!(policy instanceof CheckingPolicy)) {
                throw new IllegalArgumentException("Invalid access");
            }

            checkMoreThan5(member,dtype);

            while (true) {
                accountNumber = "01" + AccountNumberGenerator.generateRandomNumber();
                if (accountRepository.findByAccountNumber(accountNumber).isEmpty()) {
                    break;
                }
            }
            account = CheckingAccount.createCheckingAccount(name,accountNumber, policy, member);
        }

        if (dtype.equals("COMMODITY_ACCOUNT")){
            if(!(policy instanceof CommodityPolicy)){
                throw new IllegalArgumentException("Invalid access");
            }
            checkMoreThan5(member,dtype);

            while(true){
                accountNumber = "10"+ AccountNumberGenerator.generateRandomNumber();
                if (accountRepository.findByAccountNumber(accountNumber).isEmpty()){
                    break;
                }
            }

            account = CommodityAccount.createCommodityAccount(name,accountNumber,policy,member);

        }

        accountRepository.save(account);


        return account;
    }

    // 상품 계좌나 입출금 계좌가 5개 이상이면 예외를 발생시킴
    private void checkMoreThan5(Member member ,String dtype) throws AccountMoreThanFive{
        Long count = 0L;

        if (dtype.equals("CHECKING_ACCOUNT")){
            count = accountRepository.countChecking(member);

        }
        else if (dtype.equals("COMMODITY_ACCOUNT")){
            count = accountRepository.countCommodity(member);

        }

        if (count>=5L){
            throw new AccountMoreThanFive();
        }
    }
}
