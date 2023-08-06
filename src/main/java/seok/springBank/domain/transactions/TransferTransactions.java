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
@DiscriminatorValue(value="TRANSFER")
public class TransferTransactions  extends Transactions{


    public static TransferTransactions makeTransferTransaction(String name,Long amount,Account fromAccount, Account toAccount){
        TransferTransactions transactions = new TransferTransactions();

        transactions.setTransactionsName(name);
        transactions.setAmount(amount);
        transactions.setFromAccount(fromAccount);
        transactions.setToAccount(toAccount);
        transactions.setReceiverBalance(toAccount.getBalance());
        transactions.setSenderBalance(fromAccount.getBalance());

        return transactions;
    }


}