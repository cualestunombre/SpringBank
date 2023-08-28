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
    public static LoanTransactions createLoanTransaction(Account account, Long amount,String name,Boolean receiver){
        LoanTransactions loanTransactions = new LoanTransactions();
        if (receiver){
            loanTransactions.setTransactionsName(name);
            loanTransactions.setAmount(amount);
            loanTransactions.setToAccount(account);
            loanTransactions.setReceiverBalance(account.getBalance());
        }
        else{
            loanTransactions.setTransactionsName(name);
            loanTransactions.setAmount(amount);
            loanTransactions.setFromAccount(account);
            loanTransactions.setSenderBalance(account.getBalance());
        }

        return loanTransactions;
    }

}