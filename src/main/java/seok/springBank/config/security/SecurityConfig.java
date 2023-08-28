package seok.springBank.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import seok.springBank.repository.memberRepository.MemberRepositoryV2;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class SecurityConfig {

    @Autowired MemberRepositoryV2 memberRepository;
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public UserDetailsService userDetailsService(){
        return new JpaUserDetailsService(memberRepository);
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,LoginSuccessHandler loginSuccessHandler,
                                                   LoginFailureHandler loginFailureHandler) throws Exception {

        http
                .userDetailsService(userDetailsService())
                .formLogin(c->{
                    c.loginPage("/auth/login");
                    c.loginProcessingUrl("/auth/login");
                    // 로그인 성공시
                    c.successHandler(loginSuccessHandler);
                    // 로그인 실패시
                    c.failureHandler(loginFailureHandler);
                    c.usernameParameter("email");
                    c.passwordParameter("password");
                })
                .logout(logout->{
                    logout.logoutUrl("/auth/logout");
                    logout.logoutSuccessUrl("/");
                    logout.deleteCookies("JSESSIONID");

                })
                .authorizeHttpRequests(auth->{
                    auth
                            .mvcMatchers("/auth/**")
                            .permitAll()
                            .mvcMatchers("/css/**")
                            .permitAll()
                            .mvcMatchers("/")
                            .permitAll()
                            .mvcMatchers("/error/**")
                            .permitAll()
                            .anyRequest()
                            .authenticated();
                })
                .csrf(Customizer.withDefaults())
                .exceptionHandling(e->{
                    e.authenticationEntryPoint(new AuthenticationEntryPoint() {
                        // 세션 및 csrf 인증 실패시 -> 재로그인 전략
                        @Override
                        public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
                            authException.printStackTrace();
                            response.sendRedirect("/auth/login");
                        }
                    });
                    e.accessDeniedHandler(new AccessDeniedHandler() {
                        @Override
                        public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
                            accessDeniedException.printStackTrace();
                        }


                });
                });
        return http.build();
    }
}
