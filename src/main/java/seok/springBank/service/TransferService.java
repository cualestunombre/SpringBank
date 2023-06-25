package seok.springBank.service;

import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Check;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import seok.springBank.domain.account.CheckingAccount;
import seok.springBank.domain.transactions.TransferTransactions;
import seok.springBank.repository.accountRepository.AccountRepositoryV2;
import seok.springBank.repository.memberRepository.MemberRepositoryV2;
import seok.springBank.repository.transactionRepository.TransactionRepositoryV2;

@Transactional(isolation = Isolation.SERIALIZABLE)
@RequiredArgsConstructor
@Service
public class TransferService {
    private final AccountRepositoryV2 accountRepository;

    private final AccountService accountService;
    private final MemberRepositoryV2 memberRepository;
    private final TransactionRepositoryV2 transactionRepository;

    public void sendMoneyChecking(String fromAccountNumber, String toAccountNumber, Long amount){

        accountService.isMoneyEnough(fromAccountNumber,amount);

        CheckingAccount fromAccount = (CheckingAccount) accountRepository.findByAccountNumber(fromAccountNumber).get(0);
        CheckingAccount toAccount  = (CheckingAccount) accountRepository.findByAccountNumber(toAccountNumber).get(0);

        fromAccount.setBalance(fromAccount.getBalance()-amount);
        toAccount.setBalance(toAccount.getBalance()+amount);

        TransferTransactions transaction = TransferTransactions.makeTransferTransaction("이체",amount,fromAccount,toAccount);
        transactionRepository.save(transaction);



    }
}
