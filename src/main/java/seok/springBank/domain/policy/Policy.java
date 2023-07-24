package seok.springBank.domain.policy;

import lombok.Getter;
import lombok.Setter;
import seok.springBank.domain.BaseEntity;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Inheritance(strategy=InheritanceType.JOINED)
@DiscriminatorColumn(name = "dtype")
public abstract class Policy extends BaseEntity {
    @Id
    @GeneratedValue @Column(name = "policy_id")
    private Long id;
    @Column(nullable = false)
    private Double interestRate;
    @Column(nullable = false,unique = true)
    private String policyName;




}
