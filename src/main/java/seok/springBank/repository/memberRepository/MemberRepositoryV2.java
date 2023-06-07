package seok.springBank.repository.memberRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import seok.springBank.domain.member.Member;

public interface MemberRepositoryV2 extends JpaRepository<Member, Long> {

    Member findByEmail(String email);



}
