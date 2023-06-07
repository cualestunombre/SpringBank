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
        if(policySaveForm.getInterestRate()==null || policySaveForm.getPeriod()==null){
            throw new IllegalArgumentException("Invalid Access");
        }
        CheckingPolicy checkingPolicy = new CheckingPolicy();
        checkingPolicy.setInterestRate(policySaveForm.getInterestRate());
        checkingPolicy.setPeriod(policySaveForm.getPeriod());
        return checkingPolicy;
    }


}