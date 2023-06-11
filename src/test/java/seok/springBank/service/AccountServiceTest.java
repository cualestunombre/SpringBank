package seok.springBank.service;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import seok.springBank.domain.account.Account;
import seok.springBank.domain.account.AccountSaveForm;
import seok.springBank.domain.account.CheckingAccount;
import seok.springBank.domain.member.Member;
import seok.springBank.domain.member.MemberSaveForm;
import seok.springBank.domain.policy.CheckingPolicy;
import seok.springBank.domain.policy.Policy;
import seok.springBank.repository.accountRepository.AccountRepositoryV2;
import seok.springBank.repository.memberRepository.MemberRepository;
import seok.springBank.repository.memberRepository.MemberRepositoryV2;
import seok.springBank.repository.policyRepository.PolicyRepository;
import seok.springBank.repository.policyRepository.PolicyRepositoryV2;

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
    MemberRepositoryV2 memberRepository;

    @Autowired
    PolicyRepositoryV2 policyRepositoryV2;

    @Autowired
    AccountRepositoryV2 accountRepository;


    @Test
    @Transactional(isolation = Isolation.SERIALIZABLE)
    void findMyCheckingTest(){
        Policy policy = new CheckingPolicy();
        policyRepositoryV2.save(policy);
        Member member = new Member();
        member.setName("seokwoo");
        member.setEmail("12342@asdsa.com");
        member.setPassword("asdsadsd");
        CheckingAccount checkingAccount = CheckingAccount.createCheckingAccount("저축","1234",policy,member);
        memberRepository.save(member);
        accountRepository.save(checkingAccount);
        CheckingAccount checkingAccount1 = CheckingAccount.createCheckingAccount("소비","5678",policy,member);
        accountRepository.save(checkingAccount1);
        assertThat(accountService.getCheckingAccounts(member.getId()).size()).isEqualTo(2);
        accountService.getCheckingAccounts(member.getId()).forEach(ele->System.out.println(ele.getAccountNumber()));



    }
}
