package seok.springBank.controllers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import seok.springBank.domain.member.Member;
import seok.springBank.domain.member.MemberLoginForm;
import seok.springBank.domain.member.MemberSaveForm;
import seok.springBank.service.EmailService;
import seok.springBank.service.MemberService;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final MemberService memberService;

    private final EmailService emailService;

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

        System.out.println(form.getEmail());
        if(!bindingResult.hasErrors()){
            Member savedMember = memberService.signup(form);
            if(savedMember==null){
                bindingResult.rejectValue("email","SameEmail","중복된 이메일 입니다");
            }
        }

        if(!bindingResult.hasErrors()){
            String code = null;
            try{
                code = emailService.sendEmail(form.getEmail());
            }catch(Exception e){
                return "redirect:/error";
            }

            return "redirect:/auth/created?code="+code+"&email="+form.getEmail();
        }
        return "signup";
    }
    @GetMapping("/login")
    public String getLogin(Model model){
        model.addAttribute("member",new MemberLoginForm());
        return "login";
    }
    @PostMapping("/login")
    public String handleLogin(@ModelAttribute("member") MemberLoginForm form,BindingResult bindingResult,Model model, HttpServletRequest request,
    @RequestParam(defaultValue = "") String redirectURL){
        Member member = memberService.login(form,request);
        if(member == null){
            bindingResult.reject("LoginFail","아이디와 비밀번호를 확인해 주세요");
            return "login";
        }
        return "redirect:" + (redirectURL.isEmpty() ? "/" : redirectURL);


    }
    @GetMapping("/created")
    public String memberCreated(@RequestParam String email, @RequestParam String code){
        if(!StringUtils.hasText(email)||!StringUtils.hasText(code)){
            return "redirect:/error";
        }
        if (!emailService.isValidCode(email,code)){
            return "redirect:/error";
        }
        return "memberCreated";

    }

    @GetMapping("/logout")
    public String handleLogOut(HttpServletRequest request){
        memberService.logout(request);
        return "redirect:/";
    }
    @GetMapping("/member")
    public String handleAuth(@RequestParam String code,@RequestParam String email){
        if(!StringUtils.hasText(code)||!StringUtils.hasText(email)) {System.out.println("wth");return "redirect:/error";}
        if (!emailService.isValidCode(email,code)){
            System.out.println("wtf");
            return "redirect:/error";
        }
        emailService.authMember(email,code);
        return "redirect:/";
    }
}
