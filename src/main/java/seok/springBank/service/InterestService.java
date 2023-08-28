package seok.springBank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import seok.springBank.domain.account.Account;
import seok.springBank.domain.account.CheckingAccount;
import seok.springBank.domain.account.LoanAccount;
import seok.springBank.domain.policy.CheckingPolicy;
import seok.springBank.domain.policy.LoanPolicy;
import seok.springBank.domain.transactions.InterestTransactions;
import seok.springBank.domain.transactions.LoanTransactions;
import seok.springBank.repository.accountRepository.AccountRepositoryV2;
import seok.springBank.repository.transactionRepository.TransactionRepositoryV2;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.SERIALIZABLE)
public class InterestService {
    private final AccountRepositoryV2 accountRepository;
    private final TransactionRepositoryV2 transactionRepository;



    @Scheduled(cron = "0 * * * * *")
    public void doCronWork(){
        handleLoanPrincipal();
        handleLoanExpire();
        handleCheckingInterest();
        handleLoanInterest();

    }

    // 입출금 계좌 이자 로직
    private void handleCheckingInterest(){

        List<Account> list = accountRepository
                .findAll()
                .stream()
                .filter((e)->e instanceof CheckingAccount)
                .collect(Collectors.toList());

        for (Account account : list){

            CheckingAccount checkingAccount = (CheckingAccount) account;
            CheckingPolicy policy = (CheckingPolicy) checkingAccount.getPolicy();

            if (account.getBalance()!=0L){
                Long balance = Math.round(account.getBalance() * 0.01* policy.getInterestRate()) + account.getBalance();
                checkingAccount.setBalance(balance);
            }else continue;

            InterestTransactions interestTransaction =InterestTransactions.createInterestTransactions(
                    checkingAccount,Math.round(account.getBalance() * 0.01 * policy.getInterestRate()),"예금이자"
            );

            transactionRepository.save(interestTransaction);
        }
    }

    //만료 대출 계좌 처리
    //계좌 상태 : 상환완료, 원금연체, 비연체, 연체
    private void handleLoanExpire(){
        // 상환 완료된 계좌만 가져 옴
        List<Account> list = accountRepository.findAll().stream()
                .filter(e-> e instanceof LoanAccount)
                .filter(e-> !e.getExpired())
                .filter(e-> ((LoanAccount) e).getStatus().equals("상환완료"))
                .collect(Collectors.toList());
        for (Account account: list){
            LoanAccount loanAccount = (LoanAccount) account;
            if(loanAccount.getBalance() == 0){
                loanAccount.setExpired(true);
            }
        }
    }

    //원금 연체시 원금 상환 로직

    private void handleLoanPrincipal(){
        List<Account> list = accountRepository.findAll().stream()
                .filter(e->e instanceof LoanAccount)
                .filter(e-> !e.getExpired())
                .filter(e->((LoanAccount) e).getStatus().equals("원금연체"))
                .collect(Collectors.toList());

        for (Account account: list){
            LoanAccount loanAccount = (LoanAccount) account;
            LoanPolicy loanPolicy = (LoanPolicy) loanAccount.getPolicy();
            Long balance = loanAccount.getBalance();
            Long interest = Math.round(loanAccount.getAmount()*0.01*loanPolicy.getInterestRate());
            Long overdueAmount = loanAccount.getOverdueAmount();
            if (balance < interest + loanAccount.getAmount() + overdueAmount){
                loanAccount.setOverdueAmount(loanAccount.getOverdueAmount()+interest);
                loanAccount.setOverdueCnt(loanAccount.getOverdueCnt()+1);
            }
            else{
                loanAccount.setOverdueCnt(0L);
                loanAccount.setOverdueAmount(0L);
                loanAccount.setBalance(loanAccount.getBalance()-interest-loanAccount.getAmount()-overdueAmount);
                loanAccount.setStatus("상환완료");
                LoanTransactions loanTransactions = LoanTransactions
                        .createLoanTransaction(loanAccount,(interest+overdueAmount+loanAccount.getAmount()),"원금상환",false);
                transactionRepository.save(loanTransactions);
            }
        }
    }

    //대출이자 로직
    private void handleLoanInterest(){

        List<Account> list = accountRepository.findAll().stream()
                .filter((e)->e instanceof LoanAccount)
                .filter(e-> !e.getExpired())
                .filter(e-> !((LoanAccount) e).getStatus().equals("상환완료"))
                .filter(e->!((LoanAccount)e).getStatus().equals("원금연체"))
                .collect(Collectors.toList());

        for (Account account : list){
            LoanAccount loanAccount = (LoanAccount) account;
            LoanPolicy loanPolicy = (LoanPolicy) loanAccount.getPolicy();
            Long balance = loanAccount.getBalance();
            Long interest = Math.round(loanAccount.getAmount()*0.01*loanPolicy.getInterestRate());
            // 일회의 이자를 내는 것도 불가능 할 때
            if (balance < interest){
                loanAccount.setOverdueCnt(loanAccount.getOverdueCnt()+1);
                loanAccount.setStatus("연체");
                loanAccount.setOverdueAmount(loanAccount.getOverdueAmount()+interest);
            }
            // 이미 연체 상태일 때
            else if(loanAccount.getStatus().equals("연체")){
                // 이자 + 연체금액을 모두 갚을 수 있음
                if(balance >= interest + loanAccount.getOverdueAmount()){
                    loanAccount.setStatus("비연체");
                    loanAccount.setLeftCount(loanAccount.getLeftCount()-1);
                    loanAccount.setBalance(loanAccount.getBalance()-interest-loanAccount.getOverdueAmount());
                    LoanTransactions loanTransactions = LoanTransactions.createLoanTransaction(
                            loanAccount,interest + loanAccount.getOverdueAmount(),"대출이자 + 연체이자"
                    ,false);
                    loanAccount.setOverdueAmount(0L);
                    loanAccount.setOverdueCnt(0L);
                    transactionRepository.save(loanTransactions);
                }
                else{
                    loanAccount.setOverdueCnt(loanAccount.getOverdueCnt()+1);
                    loanAccount.setOverdueAmount(loanAccount.getOverdueAmount()+interest);
                }
            }
            // 연체도 없고 이자를 내기 충분한 상황
            else{
                loanAccount.setLeftCount(loanAccount.getLeftCount()-1);
                loanAccount.setBalance(loanAccount.getBalance()-interest);
                LoanTransactions loanTransactions = LoanTransactions.createLoanTransaction(
                        loanAccount,interest,"대출이자"
                ,false);
                transactionRepository.save(loanTransactions);
            }

            if(loanAccount.getLeftCount()==0){
                if (loanAccount.getBalance() >= loanAccount.getAmount()){
                    LoanTransactions loanTransactions = LoanTransactions.createLoanTransaction(
                            account, loanAccount.getAmount(),"원금상환"
                    ,false);

                    transactionRepository.save(loanTransactions);
                    loanAccount.setBalance(loanAccount.getBalance()-loanAccount.getAmount());
                    loanAccount.setStatus("상환완료");

                }
                else{
                    loanAccount.setStatus("원금연체");
                    loanAccount.setOverdueAmount(interest);
                    loanAccount.setOverdueCnt(1L);
                }
            }

        }
    }


}
