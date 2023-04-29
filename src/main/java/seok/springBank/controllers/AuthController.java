package seok.springBank.controllers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import seok.springBank.domain.member.Member;
import seok.springBank.domain.member.MemberLoginForm;
import seok.springBank.domain.member.MemberSaveForm;
import seok.springBank.service.MemberService;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final MemberService memberService;

    @GetMapping("/signup")
    public String getSignup(Model model){
        model.addAttribute("member",new MemberSaveForm());
        return "signup";
    }
    @PostMapping("/signup")
    public String handleSignup(@Validated @ModelAttribute("member") MemberSaveForm form, BindingResult bindingResult){
        if(form.getChecked()==false){
            bindingResult.reject("CheckRequired","이용약관에 동의해 주세요");
        }
        Member savedMember = memberService.signup(form);
        if(savedMember==null){
            bindingResult.rejectValue("email","SameEmail","중복된 이메일 입니다");
        }
        if(!bindingResult.hasErrors()){

            return "redirect:/";
        }

        else{

            return "signup";
        }
    }
    @GetMapping("/login")
    public String getLogin(Model model){
        model.addAttribute("member",new MemberLoginForm());
        return "login";
    }
    @PostMapping("/login")
    public String handleLogin(@ModelAttribute("member") MemberLoginForm form,BindingResult bindingResult,Model model, HttpServletRequest request){
        Member member = memberService.login(form,request);
        if(member == null){
            bindingResult.reject("LoginFail","아이디와 비밀번호를 확인해 주세요");
            return "login";
        }
        return "redirect:/";


    }

    @GetMapping("/logout")
    public String handleLogOut(HttpServletRequest request){
        memberService.logout(request);
        return "redirect:/";
    }
}
