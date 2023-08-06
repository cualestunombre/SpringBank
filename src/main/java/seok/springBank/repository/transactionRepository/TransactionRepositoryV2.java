package seok.springBank.repository.transactionRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import seok.springBank.domain.transactions.TransactionDto;
import seok.springBank.domain.transactions.Transactions;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepositoryV2 extends JpaRepository<Transactions,Long> {
    @Query("select new seok.springBank.domain.transactions.TransactionDto" +
            "(t.fromAccount.member.name,t.toAccount.member.name,t.senderBalance,t.receiverBalance" +
            ",t.fromAccount.accountNumber,t.toAccount.accountNumber,t.transactionsName,t.createdAt,t.amount) from Transactions t left join t.toAccount left join t.fromAccount" +
            " left join t.toAccount.member left join t.fromAccount.member"+
            " where t.toAccount.id = :id or t.fromAccount.id = :id")
    public Page<TransactionDto> findTranscationsByAccountId(@Param("id")Long id, Pageable pageable);

//    private final String senderName;
//    private final String receiverName;
//    private final Long senderBalance;
//    private final Long receiverBalance;
//    private final String senderAccountNumber;
//    private final String receiverAccountNumber;
//    private final String name;
//    private final LocalDateTime time;
//    private final Long amount;

}
