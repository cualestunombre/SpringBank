package seok.springBank.domain.policy;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Getter
@Setter
@DiscriminatorValue(value = "SAVINGS_POLICY")
@Entity
public class SavingsPolicy extends Policy{
    @Column(nullable = false)
    private Long maxAmount;
    @Column(nullable = false)
    private Long maxDuration;




}