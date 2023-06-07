package seok.springBank.domain.policy;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Getter
@Setter
@DiscriminatorValue(value = "COMMODITY_POLICY")
@Entity
public class CommodityPolicy extends Policy{
    public static CommodityPolicy createCommodityPolicy(PolicySaveForm policySaveForm){
        if(policySaveForm.getInterestRate()==null || policySaveForm.getPeriod()==null){
            throw new IllegalArgumentException("Invalid Access");
        }
        CommodityPolicy commodityPolicy = new CommodityPolicy();
        commodityPolicy.setPeriod(policySaveForm.getPeriod());
        commodityPolicy.setInterestRate(policySaveForm.getInterestRate());
        return commodityPolicy;
    }


}