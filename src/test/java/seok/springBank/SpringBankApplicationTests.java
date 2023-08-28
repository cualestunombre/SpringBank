package seok.springBank;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest

class SpringBankApplicationTests {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Test
    public void test(){
        String abc = "dntjrdn78";
        String encrypted = passwordEncoder.encode(abc);
        System.out.println(abc);
        System.out.println(encrypted);
        assertThat(passwordEncoder.matches(abc,encrypted)).isTrue();
    }



}
