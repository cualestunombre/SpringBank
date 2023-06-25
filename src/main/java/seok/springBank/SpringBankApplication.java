package seok.springBank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import seok.springBank.domain.account.CheckingAccount;
import seok.springBank.domain.account.CommodityAccount;
import seok.springBank.domain.member.Member;
import seok.springBank.domain.member.MemberSaveForm;
import seok.springBank.domain.policy.Policy;
import seok.springBank.domain.policy.PolicySaveForm;
import seok.springBank.repository.accountRepository.AccountRepositoryV2;
import seok.springBank.service.AccountService;
import seok.springBank.service.MemberService;
import seok.springBank.service.PolicyService;

@SpringBootApplication
@EnableJpaAuditing
public class SpringBankApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBankApplication.class, args);
	}

	@Component
	public static class postCon{
		@Autowired
		MemberService memberService;
		@Autowired
		PolicyService policyService;

		@Autowired
		AccountService accountService;

		@Autowired
		AccountRepositoryV2 accountRepository;

		@EventListener(ApplicationReadyEvent.class)
		@Transactional
		public void devInit(){
			PolicySaveForm checkingSaveForm = new PolicySaveForm();
			PolicySaveForm commoditySaveForm = new PolicySaveForm();
			checkingSaveForm.setDtype("CHECKING_POLICY");
			checkingSaveForm.setInterestRate(1.0);
			checkingSaveForm.setPeriod("1HOUR");
			commoditySaveForm.setDtype("COMMODITY_POLICY");
			commoditySaveForm.setInterestRate(0.5);
			commoditySaveForm.setPeriod("1HOUR");
			Policy checkPolicy =  policyService.makePolicy(checkingSaveForm);
			Policy commodityPolicy = policyService.makePolicy(commoditySaveForm);


			MemberSaveForm member = new MemberSaveForm();
			member.setEmail("1dilumn0@gmail.com");
			member.setName("우석우");
			member.setPassword("dntjrdn78");
			Member newMember = memberService.signup(member);
			newMember.setAuthenticated(true);

			MemberSaveForm member2 = new MemberSaveForm();
			member2.setEmail("1usian0@naver.com");
			member2.setName("임현수");
			member2.setPassword("dntjrdn78");
			Member newMember2 = memberService.signup(member2);
			newMember2.setAuthenticated(true);

			CheckingAccount checkingAccount2 = CheckingAccount.createCheckingAccount("입출금","010000000000000",checkPolicy,newMember2);
			accountRepository.save(checkingAccount2);

			CheckingAccount checkingAccount = CheckingAccount.createCheckingAccount("입출금","010101010101010",checkPolicy,newMember);
			accountRepository.save(checkingAccount);
			CommodityAccount commodityAccount = CommodityAccount.createCommodityAccount("주식","111111111111111",commodityPolicy,newMember);
			accountRepository.save(commodityAccount);



		}

		@EventListener(ApplicationReadyEvent.class)
		public void policyInit(){


		}



	}
}
