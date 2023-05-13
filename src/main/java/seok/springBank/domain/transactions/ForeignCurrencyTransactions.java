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
@DiscriminatorValue(value="FOREIGN_CURRENCY")
public class ForeignCurrencyTransactions  extends Transactions{
    @JoinColumn
    @ManyToOne
    Account toAccount ;


}