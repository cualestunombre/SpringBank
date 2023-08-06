package seok.springBank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import seok.springBank.domain.account.Account;
import seok.springBank.domain.account.LoanAccount;
import seok.springBank.domain.member.Member;
import seok.springBank.domain.member.MemberLoginForm;
import seok.springBank.domain.member.MemberSaveForm;
import seok.springBank.repository.accountRepository.AccountRepositoryV2;
import seok.springBank.repository.memberRepository.MemberRepository;
import seok.springBank.repository.memberRepository.MemberRepositoryV2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.SERIALIZABLE)
public class MemberService {
    private final MemberRepositoryV2 memberRepository;
    private final AccountRepositoryV2 accountRepository;

    //회원가입 로직
    public Member signup(MemberSaveForm saveForm){
        Member member = new Member();
        member.setEmail(saveForm.getEmail());
        member.setName(saveForm.getName());
        member.setPassword(saveForm.getPassword());
        Member sameEmailMember = memberRepository.findByEmail(saveForm.getEmail());
        if(sameEmailMember == null){
            memberRepository.save(member);
            return member;
        }
        return null;
    }

    //회원의 연체 여부 조회 로직
    public Boolean checkLatePayment(Member member){
        List<Account> accounts = accountRepository.findByMember(member)
                .stream()
                .filter(e->e instanceof LoanAccount)
                .filter(e->!e.getExpired())
                .filter(e->((LoanAccount) e).getStatus().equals("연체")||((LoanAccount) e).getStatus().equals("원금연체"))
                .collect(Collectors.toList());
        return accounts.size() != 0;

    }

    public Member login(MemberLoginForm loginForm, HttpServletRequest request){

        Member member = memberRepository.findByEmail(loginForm.getEmail());
        if(member==null) {return null;}
        else if(!member.getPassword().equals(loginForm.getPassword())) {return null;}
        else if(!member.getAuthenticated()) return null;
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
