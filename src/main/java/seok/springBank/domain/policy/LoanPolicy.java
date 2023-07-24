package seok.springBank.domain.policy;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Getter
@Setter
@DiscriminatorValue(value = "LOAN_POLICY")
@Entity
public class LoanPolicy extends Policy{


    @Column(nullable = false)
    private Long maxDuration;
    @Column(nullable = false)
    private Long maxAmount;

    public static LoanPolicy createLoanPolicy(PolicySaveForm form){
        LoanPolicy policy = new LoanPolicy();
        policy.setPolicyName(form.getPolicyName());
        policy.setMaxAmount(form.getMaxAmount());
        policy.setMaxDuration(form.getDuration());
        policy.setInterestRate(form.getInterestRate());
        return  policy;
    }

}
