package seok.springBank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seok.springBank.domain.member.Member;
import seok.springBank.domain.member.MemberLoginForm;
import seok.springBank.domain.member.MemberSaveForm;
import seok.springBank.repository.MemberRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    @Transactional
    public Member signup(MemberSaveForm saveForm){
        Member member = new Member();
        member.setEmail(saveForm.getEmail());
        member.setName(saveForm.getName());
        member.setPassword(saveForm.getPassword());
        Member sameEmailMember = memberRepository.findByEmail(saveForm.getEmail());
        if(sameEmailMember == null){
            return memberRepository.saveMember(member);
        }
        return null;

    }
    @Transactional
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
