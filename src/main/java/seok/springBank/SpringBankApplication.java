package seok.springBank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Component;
import seok.springBank.domain.member.Member;
import seok.springBank.domain.member.MemberSaveForm;
import seok.springBank.domain.policy.Policy;
import seok.springBank.domain.policy.PolicySaveForm;
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

		@EventListener(ApplicationReadyEvent.class)
		public void memberInit(){
			MemberSaveForm member = new MemberSaveForm();
			member.setEmail("1dilumn0@gmail.com");
			member.setName("우석우");
			member.setPassword("dntjrdn78");
			memberService.signup(member);

		}

		@EventListener(ApplicationReadyEvent.class)
		public void policyInit(){
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

		}

	}
}
