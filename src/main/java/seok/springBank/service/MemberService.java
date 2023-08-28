package seok.springBank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import seok.springBank.domain.account.Account;
import seok.springBank.domain.account.LoanAccount;
import seok.springBank.domain.etcForms.ApiDto;
import seok.springBank.domain.etcForms.Role;
import seok.springBank.domain.member.Member;
import seok.springBank.domain.member.MemberLoginForm;
import seok.springBank.domain.member.MemberSaveForm;
import seok.springBank.exceptions.member.AlreadyHasApiKey;
import seok.springBank.repository.accountRepository.AccountRepositoryV2;
import seok.springBank.repository.memberRepository.MemberRepository;
import seok.springBank.repository.memberRepository.MemberRepositoryV2;
import java.security.MessageDigest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.SERIALIZABLE)
public class MemberService {
    private final MemberRepositoryV2 memberRepository;
    private final AccountRepositoryV2 accountRepository;

    private final PasswordEncoder passwordEncoder;

    //회원가입 로직
    public Member signup(MemberSaveForm saveForm){
        Member member = new Member();
        member.setRole(Role.ROLE_MEMBER);
        member.setEmail(saveForm.getEmail());
        member.setName(saveForm.getName());
        member.setPassword(passwordEncoder.encode(saveForm.getPassword()));
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
    public String getApiKey(Member member){
        Member findMember = memberRepository.findById(member.getId())
                .orElseThrow(()->new IllegalArgumentException("Invalid Access"));
        return findMember.getApiKey();

    }
    public ApiDto makeApiKey(Member member){
        Member findMember = memberRepository.findById(member.getId())
                .orElseThrow(()->new IllegalArgumentException("Invalid Access"));
        if (findMember.getApiKey()!=null || findMember.getApiPassword()!=null){
            throw new AlreadyHasApiKey("Api key already exists");
        }
        String apiKey = getToken();
        String password = getToken();
        findMember.setApiKey(apiKey);
        findMember.setApiPassword(password);

        return new ApiDto(apiKey,password);

    }

    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session==null){
            return;
        }
        session.invalidate();
        return ;
    }
    private String getToken(){
        try {
            // 랜덤한 바이트 배열 생성
            byte[] randomBytes = generateRandomBytes(32); // 예시로 32바이트 생성

            // 해시 함수 선택 (SHA-256 사용)
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

            // 해시 계산
            byte[] hashBytes = messageDigest.digest(randomBytes);

            // 해시 값을 16진수 문자열로 변환하여 출력
            return bytesToHexString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Invalid Access");
        }
    }

    private byte[] generateRandomBytes(int length) {
        byte[] randomBytes = new byte[length];
        new SecureRandom().nextBytes(randomBytes);
        return randomBytes;
    }

    // 바이트 배열을 16진수 문자열로 변환
    private String bytesToHexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }
}
