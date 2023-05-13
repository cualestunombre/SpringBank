package seok.springBank.domain.member;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import seok.springBank.domain.account.Account;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {
    @GeneratedValue
    @Id
    Integer Id;

    @Column(name="name",nullable = false)
    String name;

    @Column(name="email",unique = true,nullable = false)
    String email;
    @Column(name="password",nullable = false)
    String password;

    @OneToMany(mappedBy = "member")
    List<Account> accounts = new ArrayList<>();



}
