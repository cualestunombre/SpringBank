package seok.springBank.domain.account;

import lombok.Getter;
import lombok.Setter;
import seok.springBank.domain.member.Member;
import seok.springBank.domain.policy.Policy;

import javax.persistence.*;

import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name="dtype")
@Getter
@Setter
public abstract class Account {
    @Id
    @GeneratedValue
    private Long id;

    @JoinColumn(name = "member_id")
    @ManyToOne
    private Member member;

    @JoinColumn(name="policy_id")
    @ManyToOne
    private Policy policy;

    @Column(nullable = false)
    private String accountNumber;

    @Column(nullable = false)
    private Long balance;


    @Column(nullable = false)
    private LocalDateTime createdAt;


    private LocalDateTime nextTime;
}
