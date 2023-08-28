package seok.springBank.controllers.mainControllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import seok.springBank.config.argumentresolver.Login;
import seok.springBank.domain.etcForms.ApiDto;
import seok.springBank.domain.member.Member;
import seok.springBank.service.MemberService;


@RequiredArgsConstructor
@RestController
public class MainJsonController {
    private final MemberService memberService;
    @PostMapping("/api/key")
    public ApiDto makeApiKey(@Login Member loginMember){
        return memberService.makeApiKey(loginMember);
    }
}
