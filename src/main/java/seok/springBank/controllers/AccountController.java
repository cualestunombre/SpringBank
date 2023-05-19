package seok.springBank.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import seok.springBank.config.argumentresolver.Login;
import seok.springBank.domain.member.Member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@Slf4j
@RequiredArgsConstructor
public class AccountController {

    @GetMapping("/account")
    public String getCreateAccount(HttpServletRequest req, HttpServletResponse res,@Login Member loginMember
    ,Model model) {
        model.addAttribute("loginMember",loginMember);


        return "accountOpening";

    }
}
