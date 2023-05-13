package seok.springBank.domain.foreignCurrency;

import lombok.Getter;
import lombok.Setter;
import seok.springBank.domain.account.Account;
import seok.springBank.domain.account.CommodityAccount;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class ForeignCurrency {
    @Id
    @GeneratedValue
    @Column(name="foreign_currency_id")
    private Long id;
    @ManyToOne
    @JoinColumn(name="account_id",nullable = false)
    private Account account;

    @Column(nullable = false)
    private Long amount;

    @Column(nullable = false)
    private LocalDateTime createdAt;


    @Column(nullable = false)
    private String currencyType ;

}
