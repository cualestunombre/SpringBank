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
import seok.springBank.domain.transactions.LoanTransactions;
import seok.springBank.exceptions.account.*;
import seok.springBank.repository.accountRepository.AccountRepositoryV2;
import seok.springBank.repository.memberRepository.MemberRepositoryV2;
import seok.springBank.repository.policyRepository.PolicyRepositoryV2;
import seok.springBank.repository.transactionRepository.TransactionRepositoryV2;
import seok.springBank.utility.AccountNumberGenerator;
import seok.springBank.utility.LoanTypeInterpreter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional(isolation = Isolation.SERIALIZABLE)
@RequiredArgsConstructor
@Service
public class AccountService {
    private final AccountRepositoryV2 accountRepository;
    private final MemberRepositoryV2 memberRepository;
    private final PolicyRepositoryV2 policyRepository;

    private final TransactionRepositoryV2 transactionRepository;

    // 계좌 ID로 계좌를 가져오는 로직
    public Optional<Account> getAccountsById(Long id){
        return  accountRepository.findById(id);
    }


    //계좌번호를 통해서 대출계좌를 가져오는 로직
    public LoanAccount getLoanAccountByAccountNumber(String accountNumber){
        return accountRepository.findByAccountNumber(accountNumber)
                .filter(e->e instanceof LoanAccount)
                .map(e->(LoanAccount) e)
                .stream().findFirst()
                .orElseThrow(()->{throw new IllegalArgumentException("Invalid Access");});
    }

    //특정 회원이 입출금계좌를 갖고 있는 지 확인하는 로직
    public Boolean hasCheckingAccount(Member member){
        List<Account> accounts = accountRepository.findByMember(member)
                .stream()
                .filter(e->e instanceof CheckingAccount)
                .filter(e->!e.getExpired())
                .collect(Collectors.toList());
        return accounts.size() != 0;
    }
    //대출 계좌를 생성하는 로직
    public LoanAccount createLoan(LoanAccountSaveForm saveForm,Member member){
        LoanPolicy loanPolicy = policyRepository
                .findById(saveForm.getPolicyId())
                .filter(e->e instanceof LoanPolicy)
                .map(e->(LoanPolicy) e)
                .stream().findFirst()
                .orElseThrow(()->{throw new IllegalArgumentException("Invalid Access");});
        String accountNumber;


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

        String loanName = LoanTypeInterpreter.translate("title",loanPolicy.getPolicyName());
        LoanAccount loanAccount = new LoanAccount();

        loanAccount.setPolicy(loanPolicy);
        loanAccount.setAccountNumber(accountNumber);
        loanAccount.setExpired(false);
        loanAccount.setStatus("비연체");
        loanAccount.setName(loanName);
        loanAccount.setBalance(saveForm.getAmount());
        loanAccount.setLeftCount(saveForm.getDuration());
        loanAccount.setMember(member);
        loanAccount.setAmount(saveForm.getAmount());
        loanAccount.setOverdueAmount(0L);
        loanAccount.setCreatedAt(LocalDateTime.now());
        loanAccount.setOverdueCnt(0L);

        LoanTransactions loanTransactions = LoanTransactions.createLoanTransaction(
                loanAccount, saveForm.getAmount(),"대출"
        );

        transactionRepository.save(loanTransactions);
        accountRepository.save(loanAccount);

        return loanAccount;

    }

    // 특정 정책을 가진 계좌를 가져오는 로직(expired 되지 않음)
    public List<Account> hasAccountByPolicyId(Long id){ //expire되지 않은 계좌를 찾아 옴
        Policy policy = policyRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Invalid Access"));
        List<Account> account = accountRepository.findAccountByPolicyAndNotExpired(policy);
        return account;
    }


    // 입출금계좌 여부확인 로직
    public void isCheckingAccount(String accountNumber){
        accountRepository
                .findByAccountNumber(accountNumber)
                .filter(e->e instanceof CheckingAccount)
                .orElseThrow(()->{throw new IllegalArgumentException("InvalidAccess");});
    }
    //계좌에 잔액이 충분한 지 확인하는 로직
    public void isMoneyEnough(String accountNumber,Long amount){
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(()->{throw new IllegalArgumentException("Invalid Access");});
        if (account.getBalance()>=amount) return;
        else{
            throw new BalanceNotEnoughException("Balance is not enough");
        }
    }
    //자신의 계좌인지 확인하는 로직
    public void isMyAccount(String accountNumber,Long memberId){
        Account account = accountRepository
                .findByAccountNumber(accountNumber)
                .orElseThrow(()->new IllegalArgumentException("Invalid Access"));
        if(account.getMember().getId() != memberId) throw new IllegalArgumentException("Invalid Access");

    }

    //유효한 생성 계좌인지 확인하는 로직
    public void isValidCreatedAccount(String type, String name, String number,Long memberId){
        if (!StringUtils.hasText(type)|| !StringUtils.hasText(name) || !StringUtils.hasText(number)
        || !(type.equals("COMMODITY_ACCOUNT" )|| type.equals("CHECKING_ACCOUNT") || type.equals("LOAN_ACCOUNT"))  ){
            throw new IllegalArgumentException("Invalid Access");
        }
        accountRepository
                .isValidCreatedAccount(memberId,name,number)
                .orElseThrow(()->new IllegalArgumentException("Invalid Access"));
    }
    // 계좌번호로 특정 입출금 계좌를 가져오는 로직 + 이 입출금 계좌가 현재 자신의 계좌와 같은 지 까지 검증함

    public CheckingAccount getCheckingAccountByAccountNumber(String targetAccountNumber,String myAccountNumber){
       Account account = accountRepository
               .findByAccountNumber(targetAccountNumber)
               .orElse(null);

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

    //입출금 계좌를 id로 가져오는 로직
    public CheckingAccount getCheckingAccountById(Long id){
        return (CheckingAccount) accountRepository
                .findById(id)
                .filter(e->e instanceof CheckingAccount)
                .orElseThrow(()->new IllegalArgumentException("Invalid Access"));
    }

    //특정 회원이 가진 expired되지 않은 대출계좌를 가져 옴
    public List<LoanAccount> getLoanAccounts(Member member){
        return accountRepository.findByMember(member)
                .stream()
                .filter(e->e instanceof LoanAccount)
                .filter(e->!(e.getExpired()))
                .map(e->(LoanAccount)e)
                .collect(Collectors.toList());
    }
    //특정 회원이 가진 모든 입출금계좌를 가져 옴
    public List<CheckingAccount> getCheckingAccounts(Long memberId){
        return accountRepository.findCheckingAccountById(memberId);
    }

    // 입출금 계좌또는 상품계좌를 생성하는 로직
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
