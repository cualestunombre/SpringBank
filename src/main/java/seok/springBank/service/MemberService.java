package seok.springBank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import seok.springBank.domain.member.Member;
import seok.springBank.domain.member.MemberLoginForm;
import seok.springBank.domain.member.MemberSaveForm;
import seok.springBank.repository.memberRepository.MemberRepository;
import seok.springBank.repository.memberRepository.MemberRepositoryV2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.SERIALIZABLE)
public class MemberService {
    private final MemberRepositoryV2 memberRepository;

    public Member signup(MemberSaveForm saveForm){
        Member member = new Member();
        member.setEmail(saveForm.getEmail());
        member.setName(saveForm.getName());
        member.setPassword(saveForm.getPassword());
        Member sameEmailMember = memberRepository.findByEmail(saveForm.getEmail());
        System.out.println (member.getEmail());
        if(sameEmailMember == null){
            System.out.println("wth");
            memberRepository.save(member);
            return member;
        }
        System.out.println("good");
        return null;

    }

    public Member login(MemberLoginForm loginForm, HttpServletRequest request){

        Member member = memberRepository.findByEmail(loginForm.getEmail());
        if(member==null) {return null;}
        else if(!member.getPassword().equals(loginForm.getPassword())) {return null;}
        else {
            HttpSession session = request.getSession(true);
            session.setAttribute(SessionConst.LOGIN_MEMBER,member);
            return member;
        }
    }

    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session==null){
            return;
        }
        session.invalidate();
        return ;
    }
}
