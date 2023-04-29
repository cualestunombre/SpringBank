package seok.springBank.domain.member;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Member {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    Integer Id;

    @Column(name="name",length = 10)
    String name;

    @Column(name="email",unique = true)
    String email;
    @Column(name="password",length = 20)
    String password;



}
