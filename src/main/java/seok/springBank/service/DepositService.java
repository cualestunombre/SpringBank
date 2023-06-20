package seok.springBank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import seok.springBank.domain.account.Account;
import seok.springBank.domain.account.CheckingAccount;
import seok.springBank.domain.member.Member;
import seok.springBank.domain.transactions.DepositTransactions;
import seok.springBank.repository.accountRepository.AccountRepositoryV2;
import seok.springBank.repository.memberRepository.MemberRepositoryV2;
import seok.springBank.repository.transactionRepository.TransactionRepositoryV2;

import java.util.Optional;

@Transactional(isolation = Isolation.SERIALIZABLE)
@RequiredArgsConstructor
@Service
public class DepositService {
    private final AccountRepositoryV2 accountRepository;
    private final MemberRepositoryV2 memberRepository;

    private final TransactionRepositoryV2 transactionRepository;

    public void depositMoney(Long memberId, String accountNumber ,Long amount){
        if(amount<=0) throw new IllegalArgumentException("Invalid Access");
        Member member  = memberRepository.findById(memberId).orElseThrow(()->new IllegalArgumentException("Invalid Access"));
        Account account = accountRepository.findByAccountNumberAndMember(accountNumber,member).orElseThrow(()-> new IllegalArgumentException("Invalid Access"));
        if (account instanceof CheckingAccount){
            account.setBalance(account.getBalance()+amount);
            DepositTransactions depositTransactions = DepositTransactions.createSavingsTransactions(account,amount,"입금");
            transactionRepository.save(depositTransactions);
            return;
        }
        throw new IllegalArgumentException("Invalid Access");
    }
}
