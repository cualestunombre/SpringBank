package seok.springBank.repository.policyRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import seok.springBank.domain.policy.Policy;

import java.util.Optional;

public interface PolicyRepositoryV2 extends JpaRepository<Policy,Long> {
    public Optional<Policy> findPolicyByPolicyName(String policyName);
}
