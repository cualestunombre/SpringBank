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
@DiscriminatorValue(value="INTEREST")
public class InterestTransactions  extends Transactions{
    public static InterestTransactions createInterestTransactions(Account toAccount,Long amount,String transactionName){
        InterestTransactions interestTransactions = new InterestTransactions();
        interestTransactions.setTransactionsName(transactionName);
        interestTransactions.setAmount(amount);
        interestTransactions.setToAccount(toAccount);
        interestTransactions.setReceiverBalance(toAccount.getBalance());
        return interestTransactions;
    }
}