package seok.springBank.domain.account;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalDateTime;
@Getter
@Setter
@Entity
public class LoanAccount extends Account{
    @Column(nullable = false)
    LocalDateTime endAt;
    @Column(nullable = false)
    Long leftCount;
}
