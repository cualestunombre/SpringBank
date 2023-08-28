package seok.springBank.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import seok.springBank.domain.member.Member;
import seok.springBank.repository.memberRepository.MemberRepositoryV2;

import java.util.Collection;
import java.util.List;

@Transactional
@RequiredArgsConstructor
public class JpaUserDetailsService implements UserDetailsService {
    private final MemberRepositoryV2 memberRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email);
        if (member == null){
            throw new UsernameNotFoundException("Invalid User");
        }
        return new JpaUserDetails(member);
    }

    @RequiredArgsConstructor
    static class JpaUserDetails implements UserDetails{
        private final Member member;
        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return List.of(()->member.getRole().toString());
        }

        @Override
        public String getPassword() {
            return member.getPassword();
        }

        @Override
        public String getUsername() {
            return member.getEmail();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }
}
