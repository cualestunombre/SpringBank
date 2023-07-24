package seok.springBank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seok.springBank.domain.account.Account;
import seok.springBank.domain.account.CheckingAccount;
import seok.springBank.domain.policy.CheckingPolicy;
import seok.springBank.domain.transactions.InterestTransactions;
import seok.springBank.repository.accountRepository.AccountRepositoryV2;
import seok.springBank.repository.transactionRepository.TransactionRepositoryV2;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class InterestService {
    private final AccountRepositoryV2 accountRepository;
    private final TransactionRepositoryV2 transactionRepository;

    @Scheduled(cron = "0 * * * * *")
    public void handleCheckingInterest(){
        List<Account> list = accountRepository.findAll().stream().filter((e)->e instanceof CheckingAccount).collect(Collectors.toList());
        for (Account account : list){
            CheckingAccount checkingAccount = (CheckingAccount) account;
            CheckingPolicy policy = (CheckingPolicy) checkingAccount.getPolicy();
            if (account.getBalance()!=0L){
                Long balance = Math.round(account.getBalance() * 0.01* policy.getInterestRate()) + account.getBalance();
                checkingAccount.setBalance(balance);
            }else continue;
            InterestTransactions interestTransaction = new InterestTransactions();
            interestTransaction.setToAccount(checkingAccount);
            interestTransaction.setTransactionsName("예금이자");
            interestTransaction.setAmount(Math.round(account.getBalance() * 0.01 * policy.getInterestRate()));
            transactionRepository.save(interestTransaction);
        }

    }

}
