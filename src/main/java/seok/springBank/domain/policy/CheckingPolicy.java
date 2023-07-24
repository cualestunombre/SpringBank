package seok.springBank.domain.policy;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Getter
@Setter
@DiscriminatorValue(value = "CHECKING_POLICY")
@Entity
public class CheckingPolicy extends Policy{
    public static CheckingPolicy createCheckingPolicy(PolicySaveForm policySaveForm){
        CheckingPolicy checkingPolicy = new CheckingPolicy();
        checkingPolicy.setInterestRate(policySaveForm.getInterestRate());
        checkingPolicy.setPolicyName(policySaveForm.getPolicyName());
        return checkingPolicy;
    }


}