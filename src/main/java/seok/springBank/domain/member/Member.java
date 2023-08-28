package seok.springBank.domain.member;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import seok.springBank.domain.BaseEntity;
import seok.springBank.domain.account.Account;
import seok.springBank.domain.etcForms.Role;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member extends BaseEntity {
    @GeneratedValue
    @Id
    @Column(name="member_id")
    Long id;

    @Column(name="name",nullable = false)
    String name;

    @Column(name="email",unique = true,nullable = false)
    String email;
    @Column(name="password",nullable = false)
    String password;

    @OneToMany(mappedBy = "member")
    List<Account> accounts = new ArrayList<>();

    Boolean authenticated = false;

    @Column(name="api_key",unique = true)
    String apiKey;
    @Column(name="api_password")
    String apiPassword;

    @Column(name="role",nullable = false)
    @Enumerated(value = EnumType.STRING)
    Role role;


}
