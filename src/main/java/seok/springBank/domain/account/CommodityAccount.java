package seok.springBank.domain.account;

import lombok.Getter;
import lombok.Setter;
import seok.springBank.domain.member.Member;
import seok.springBank.domain.policy.Policy;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@DiscriminatorValue(value = "COMMODITY_ACCOUNT")
public class CommodityAccount extends Account{
    public static CommodityAccount createCommodityAccount(String accountNumber, Policy policy, Member member){
        CommodityAccount commodityAccount = new CommodityAccount();
        commodityAccount.setPolicy(policy);
        commodityAccount.setMember(member);
        commodityAccount.setCreatedAt(LocalDateTime.now());
        commodityAccount.setAccountNumber(accountNumber);
        commodityAccount.setBalance(0L);
        return commodityAccount;
    }
}
