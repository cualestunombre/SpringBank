package seok.springBank.domain.account;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDateTime;
@Getter
@Setter
@Entity
@DiscriminatorValue(value = "LOAN_ACCOUNT")
public class LoanAccount extends Account{

    @Column(nullable = false)
    Long leftCount;

    @Column(nullable = false)
    String status;

    @Column(nullable = false)
    Long overdueCnt;

    @Column(nullable = false)
    Long amount;

    @Column(nullable = false)
    Long overdueAmount;


}
