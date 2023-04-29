package seok.springBank.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import seok.springBank.config.argumentresolver.Login;
import seok.springBank.domain.member.Member;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller
public class MainController {
    @GetMapping("/")
    public String getWelcome(@Login Member loginMember, Model model) {
        model.addAttribute("loginMember",loginMember);
        return "welcome";
    }

}
