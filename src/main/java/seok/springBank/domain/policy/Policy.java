package seok.springBank.domain.policy;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
public abstract class Policy {
    @Id
    @GeneratedValue @Column(name = "policy_id")
    private Long id;

    private Double interestRate;


}
