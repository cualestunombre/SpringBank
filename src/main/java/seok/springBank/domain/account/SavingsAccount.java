package seok.springBank.domain.account;

import lombok.Getter;
import lombok.Setter;
import seok.springBank.domain.policy.Policy;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@DiscriminatorValue(value = "SAVINGS_ACCOUNT")
public class SavingsAccount extends Account {
    @Column(nullable = false)
    LocalDateTime endAt;
    @Column(nullable = false)
    Long leftCount;


}
