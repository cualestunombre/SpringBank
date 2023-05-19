package seok.springBank.service;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import seok.springBank.domain.account.Account;
import seok.springBank.domain.account.AccountSaveForm;
import seok.springBank.domain.member.Member;
import seok.springBank.domain.member.MemberSaveForm;
import seok.springBank.domain.policy.CheckingPolicy;
import seok.springBank.domain.policy.Policy;
import seok.springBank.repository.memberRepository.MemberRepository;
import seok.springBank.repository.policyRepository.PolicyRepository;

import javax.persistence.EntityManager;

@SpringBootTest
public class AccountServiceTest {

    @Autowired
    AccountService accountService;
    @Autowired
    MemberService memberService;

    @Autowired
    PolicyRepository policyRepository;

    @Autowired
    MemberRepository memberRepository;


    @Test
    @Transactional(isolation = Isolation.SERIALIZABLE)
    void makeAccounTest(){
        MemberSaveForm memberSaveForm = new MemberSaveForm();
        memberSaveForm.setName("seokwoo");
        memberSaveForm.setPassword("dddd");
        memberSaveForm.setEmail("1dilumn0@gmail.com");
        Member member = memberService.signup(memberSaveForm);

        Policy policy = new CheckingPolicy();
        policy.setInterestRate(3.5);
        policyRepository.savePolicy(policy);

        AccountSaveForm accountSaveForm = new AccountSaveForm();
        accountSaveForm.setDtype("CHECKING_ACCOUNT");
        accountSaveForm.setPolicyId(policy.getId());

        Account account = accountService.makeAccount(accountSaveForm,member.getId(),policy.getId());
        System.out.println(account.getAccountNumber());
        System.out.println(account.getMember().getName());

    }
}
