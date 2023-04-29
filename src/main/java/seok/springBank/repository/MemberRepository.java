package seok.springBank.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import seok.springBank.domain.member.Member;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

@Repository
@RequiredArgsConstructor
public class MemberRepository {
    private final EntityManager em;

    public Member saveMember(Member member){
        em.persist(member);
        return member;
    }

    public Member findByEmail(String email){
        String jpql = "SELECT m FROM Member m WHERE m.email=:email";
        TypedQuery<Member> query = em.createQuery(jpql, Member.class);
        query.setParameter("email",email);
        return query.getResultList().stream().findAny().orElse(null);

    }


}
