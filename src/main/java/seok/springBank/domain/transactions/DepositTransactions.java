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
@DiscriminatorValue(value="DEPOSIT")
public class DepositTransactions  extends Transactions{


    public static DepositTransactions createSavingsTransactions(Account toAccount,Long amount,String transactionName){
        DepositTransactions depositTransactions = new DepositTransactions();

        depositTransactions.setAmount(amount);
        depositTransactions.setTransactionsName(transactionName);
        depositTransactions.setToAccount(toAccount);
        depositTransactions.setReceiverBalance(amount);

        return depositTransactions;

    }

}
