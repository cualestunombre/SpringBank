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
@DiscriminatorValue(value = "CHECKING_ACCOUNT")
public class CheckingAccount extends Account {
    public static CheckingAccount createCheckingAccount(String name,String accountNumber, Policy policy, Member member){
        CheckingAccount checkingAccount = new CheckingAccount();
        checkingAccount.setPolicy(policy);
        checkingAccount.setMember(member);
        checkingAccount.setCreatedAt(LocalDateTime.now());
        checkingAccount.setAccountNumber(accountNumber);
        checkingAccount.setBalance(0L);
        checkingAccount.setName(name);
        return checkingAccount;
    }
}
