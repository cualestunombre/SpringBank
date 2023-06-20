package seok.springBank.repository.transactionRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import seok.springBank.domain.transactions.Transactions;

public interface TransactionRepositoryV2 extends JpaRepository<Transactions,Long> {

}
