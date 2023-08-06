package seok.springBank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import seok.springBank.domain.account.Account;
import seok.springBank.domain.account.LoanAccount;
import seok.springBank.domain.member.Member;

import seok.springBank.domain.transactions.LoanTransactions;
import seok.springBank.domain.transactions.TransactionDto;
import seok.springBank.domain.transactions.Transactions;
import seok.springBank.repository.accountRepository.AccountRepositoryV2;
import seok.springBank.repository.memberRepository.MemberRepositoryV2;
import seok.springBank.repository.transactionRepository.TransactionRepositoryV2;

import java.awt.print.Pageable;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.SERIALIZABLE)
public class TransactionsService {
    private final TransactionRepositoryV2 transactionRepository;
    private final AccountRepositoryV2 accountRepository;

    //특정 멤버의 특정 계좌의 트랜잭션 기록을 페이지로 가져오는 로직
    public List<TransactionDto> getTransactions(Long id, Long page, Member member){
       LoanAccount account =(LoanAccount) accountRepository.findById(id)
                .stream().filter((e)->e instanceof LoanAccount)
                .filter(e->e.getMember().getId() == member.getId())
                .findFirst()
                .orElseThrow(()->{throw new IllegalArgumentException("Invalid Access");});
       PageRequest pageRequest = PageRequest.of(Math.toIntExact(page)-1,5,Sort.by(Sort.Direction.ASC,"createdAt"));
       Page<TransactionDto> transactionDtos = transactionRepository.findTranscationsByAccountId(id,pageRequest);
       return transactionDtos.getContent();
    }
    public Long getTransactionsPage(Long id){
        PageRequest pageRequest = PageRequest.of(1,5,Sort.by(Sort.Direction.ASC,"createdAt"));
        Page<TransactionDto> transactionDtos = transactionRepository.findTranscationsByAccountId(id,pageRequest);
        return (long) transactionDtos.getTotalPages();
    }
}
