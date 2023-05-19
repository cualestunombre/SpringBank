package seok.springBank.repository.policyRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import seok.springBank.domain.policy.Policy;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class PolicyRepository {
    private final EntityManager em;

    public Policy findById(Long policyId){
        return em.find(Policy.class,policyId);
    }

    public Policy savePolicy(Policy policy){
        em.persist(policy);
        return policy;
    }
}
