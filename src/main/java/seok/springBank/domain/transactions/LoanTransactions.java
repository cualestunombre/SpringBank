package seok.springBank.domain.transactions;

import lombok.Getter;
import lombok.Setter;
import seok.springBank.domain.account.Account;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@DiscriminatorValue(value="LOAN")
public class LoanTransactions  extends Transactions{
    public static LoanTransactions createLoanTransaction(Account toAccount, Long amount,String name){
        LoanTransactions loanTransactions = new LoanTransactions();
        loanTransactions.setTransactionsName(name);
        loanTransactions.setAmount(amount);
        loanTransactions.setToAccount(toAccount);
        loanTransactions.setReceiverBalance(toAccount.getBalance());
        return loanTransactions;
    }

}