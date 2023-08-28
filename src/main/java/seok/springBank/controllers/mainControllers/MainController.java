package seok.springBank.controllers.mainControllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import seok.springBank.config.argumentresolver.Login;
import seok.springBank.domain.member.Member;
import seok.springBank.service.MemberService;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MainController {
    private final MemberService memberService;

    // 메인 페이지 렌더링
    @GetMapping("/")
    public String getWelcome(@Login Member loginMember, Model model) {
        model.addAttribute("loginMember",loginMember);
        return "welcome";
    }
    // 성공 페이지 렌더링
    @GetMapping("/success/{path}")
    public String getSuccess(@Login Member loginMember, Model model, @PathVariable(value = "path") String path){
        path = URLDecoder.decode(path);
        model.addAttribute("object",path);
        model.addAttribute("loginMember",loginMember);
        return "success";
    }
    @GetMapping("/api")
    public String getApiPage(@Login Member loginMember, Model model){
        model.addAttribute("loginMember",loginMember);
        return "api";
    }
    @GetMapping("/api/key")
    public String getApi(@Login Member loginMember,Model model){
        String apiKey = memberService.getApiKey(loginMember);
        model.addAttribute("loginMember",loginMember);
        model.addAttribute("apiKey",apiKey);
        return "makeApi";
    }


}
