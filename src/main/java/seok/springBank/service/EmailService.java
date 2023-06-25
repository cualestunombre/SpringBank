package seok.springBank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import seok.springBank.domain.member.Member;
import seok.springBank.repository.memberRepository.MemberRepositoryV2;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.SERIALIZABLE)
public class EmailService {
    private final MemberRepositoryV2 memberRepository;

    //의존성 주입을 통해서 필요한 객체를 가져온다.
    private final JavaMailSender emailSender;
    // 타임리프를사용하기 위한 객체를 의존성 주입으로 가져온다
    private final SpringTemplateEngine templateEngine;

    private ConcurrentHashMap<String,String> authMap = new ConcurrentHashMap<>();

    public void authMember(String email, String code){
        if (authMap.containsKey(email)){
            authMap.remove(email);
        }
        Member findMember =  memberRepository.findByEmail(email);
        if (findMember == null){
            throw new IllegalArgumentException("Invalid Access");
        }
        findMember.setAuthenticated(true);

    }

    //랜덤 인증 코드 생성
    private String createCode() {
        Random random = new Random();
        StringBuffer key = new StringBuffer();

        for(int i=0;i<8;i++) {
            int index = random.nextInt(3);

            switch (index) {
                case 0 :
                    key.append((char) ((int)random.nextInt(26) + 97));
                    break;
                case 1:
                    key.append((char) ((int)random.nextInt(26) + 65));
                    break;
                case 2:
                    key.append(random.nextInt(9));
                    break;
            }
        }
        return key.toString();
    }

    //메일 양식 작성


    public MimeMessage createEmailForm(String email) throws MessagingException, UnsupportedEncodingException {

        String code = createCode(); //인증 코드 생성
        String setFrom = "springbank0625@gmail.com"; //email-config에 설정한 자신의 이메일 주소(보내는 사람)
        String title = "회원가입 인증 번호"; //제목
        MimeMessage message = emailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, email); //보낼 이메일 설정
        message.setSubject(title); //제목 설정
        message.setFrom(setFrom); //보내는 이메일
        message.setText(setContext(code,email), "utf-8", "html");
        authMap.put(email,code);
        return message;
    }
    public boolean isValidCode(String email,String code){
        if(authMap.containsKey(email) && authMap.get(email).equals(code)){
            return true;
        }
        return false;
    }

    //실제 메일 전송
    public String sendEmail(String toEmail) throws MessagingException, UnsupportedEncodingException {

        //메일전송에 필요한 정보 설정
        MimeMessage emailForm = createEmailForm(toEmail);
        //실제 메일 전송
        emailSender.send(emailForm);

        return authMap.get(toEmail); //인증 코드 반환
    }

    //타임리프를 이용한 context 설정
    public String setContext(String code,String email) {
        Context context = new Context();
        context.setVariable("code", code);
        context.setVariable("email",email);
        return templateEngine.process("mail", context); //mail.html
    }

}