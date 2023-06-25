package seok.springBank.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import seok.springBank.service.EmailService;

@Controller
@RequiredArgsConstructor
@Slf4j
public class TestController {
    private final EmailService emailService;

    @ResponseBody
    @GetMapping("/test/email")
    public String testEmail(@RequestParam("email") String email){
        try{
            emailService.sendEmail("1dilumn0@gmail.com");
        }catch(Exception e){
            log.info("not working");
        }

        return "ok";
    }

}
