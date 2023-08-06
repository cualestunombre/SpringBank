package seok.springBank.bean;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import seok.springBank.SpringBankApplication;

@SpringBootTest
public class ApplicationContextInfoTest {
    @Autowired
    private ApplicationContext applicationContext;

    @Test
    @DisplayName("모든 빈 출력하기")
    void findApplicationBean() {
        String[] beanNames = applicationContext.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            Object bean = applicationContext.getBean(beanName);
            System.out.println("name=" + beanName + " object=" + bean);
        }
    }
}
