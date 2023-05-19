package seok.springBank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import seok.springBank.domain.member.Member;
import seok.springBank.domain.member.MemberSaveForm;
import seok.springBank.service.MemberService;

@SpringBootApplication
public class SpringBankApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBankApplication.class, args);
	}

	@Component
	public static class postCon{
		@Autowired
		MemberService memberService;
		@EventListener(ApplicationReadyEvent.class)
		public void memberInit(){
			MemberSaveForm member = new MemberSaveForm();
			member.setEmail("1dilumn0@gmail.com");
			member.setName("우석우");
			member.setPassword("dntjrdn78");
			memberService.signup(member);

		}

	}
}
