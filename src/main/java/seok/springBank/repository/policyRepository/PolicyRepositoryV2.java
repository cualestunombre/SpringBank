package seok.springBank.repository.policyRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import seok.springBank.domain.policy.Policy;

public interface PolicyRepositoryV2 extends JpaRepository<Policy,Long> {

}
