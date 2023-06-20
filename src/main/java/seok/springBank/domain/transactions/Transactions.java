package seok.springBank.domain.transactions;

import lombok.Getter;
import lombok.Setter;
import seok.springBank.domain.BaseEntity;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter
@Setter
@Entity
public abstract class Transactions extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name="transactions_id")
    private Long id;


    @Column(nullable = false)
    private Long amount;

    @Column(nullable = false)
    private String transactionsName;





}
