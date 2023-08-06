package seok.springBank.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import seok.springBank.config.argumentresolver.Login;
import seok.springBank.domain.member.Member;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;

@Slf4j
@Controller
public class MainController {
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

}
