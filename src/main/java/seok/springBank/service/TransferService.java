package seok.springBank.service;

import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Check;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import seok.springBank.domain.account.CheckingAccount;
import seok.springBank.domain.account.LoanAccount;
import seok.springBank.domain.account.LoanTransferForm;
import seok.springBank.domain.member.Member;
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
    private final TransactionRepositoryV2 transactionRepository;
    // 대출 계좌에서 입출금 계좌로 송금하는 로직
    public void transferToChecking(LoanTransferForm loanTransferForm, Member member){
        // checkingAccountId:selectedChecking,amount:amount,loanName:selectedLoan
        String loanName = loanTransferForm.getLoanName();
        Long amount = loanTransferForm.getAmount();
        Long id = loanTransferForm.getCheckingAccountId();

        LoanAccount loanAccount = (LoanAccount) accountRepository.findByMember(member)
                .stream()
                .filter(e->e instanceof LoanAccount)
                .filter(e->e.getName().equals(loanName))
                .filter(e->!(e.getExpired()))
                .findFirst()
                .orElseThrow(()->{throw new IllegalArgumentException("Invalid Access");});
        CheckingAccount checkingAccount = (CheckingAccount) accountRepository.findByMember(member)
                .stream()
                .filter(e->e instanceof CheckingAccount)
                .filter(e->e.getId()==id)
                .findFirst()
                .orElseThrow(()->{throw new IllegalArgumentException("Invalid Access");});

        if(loanAccount.getBalance() < amount){
            throw new IllegalArgumentException("Invalid Access");
        }

        loanAccount.setBalance(loanAccount.getBalance() - amount);
        checkingAccount.setBalance(checkingAccount.getBalance() + amount);
        TransferTransactions transactions = TransferTransactions.makeTransferTransaction("이체",amount,loanAccount,checkingAccount);
        transactionRepository.save(transactions);
    }
    //입출금 계좌에서 대출 계좌로 송금하는 로직
    public void transferToLoan(LoanTransferForm loanTransferForm, Member member){
        String loanName = loanTransferForm.getLoanName();
        Long amount = loanTransferForm.getAmount();
        Long id = loanTransferForm.getCheckingAccountId();

        LoanAccount loanAccount = (LoanAccount) accountRepository.findByMember(member)
                .stream()
                .filter(e->e instanceof LoanAccount)
                .filter(e->e.getName().equals(loanName))
                .filter(e->!(e.getExpired()))
                .findFirst()
                .orElseThrow(()->{throw new IllegalArgumentException("Invalid Access");});
        CheckingAccount checkingAccount = (CheckingAccount) accountRepository.findByMember(member)
                .stream()
                .filter(e->e instanceof CheckingAccount)
                .filter(e->e.getId()==id)
                .findFirst().orElseThrow(()->{throw new IllegalArgumentException("Invalid Access");});

        if (checkingAccount.getBalance()<amount){
            throw new IllegalArgumentException("Invalid Access");
        }

        checkingAccount.setBalance(checkingAccount.getBalance()-amount);
        loanAccount.setBalance(loanAccount.getBalance()+amount);

        TransferTransactions transaction = TransferTransactions.makeTransferTransaction(
                "이체",amount,checkingAccount,loanAccount
        );

        transactionRepository.save(transaction);
    }

    // 입출금 계좌간 송금 로직

    public void sendMoneyChecking(String fromAccountNumber, String toAccountNumber, Long amount){

        accountService.isMoneyEnough(fromAccountNumber,amount);
        CheckingAccount fromAccount = (CheckingAccount) accountRepository
                .findByAccountNumber(fromAccountNumber)
                .filter(e->e instanceof CheckingAccount)
                .orElseThrow(()->{throw new IllegalArgumentException("Invalid Access");});
        CheckingAccount toAccount  = (CheckingAccount) accountRepository
                .findByAccountNumber(toAccountNumber)
                .filter(e->e instanceof CheckingAccount)
                .orElseThrow(()->{throw new IllegalArgumentException("Invalid Access");});
        fromAccount.setBalance(fromAccount.getBalance()-amount);
        toAccount.setBalance(toAccount.getBalance()+amount);
        TransferTransactions transaction = TransferTransactions.makeTransferTransaction("이체",amount,fromAccount,toAccount);
        transactionRepository.save(transaction);
    }
}
