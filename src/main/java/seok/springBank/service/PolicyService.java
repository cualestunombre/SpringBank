package seok.springBank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import seok.springBank.domain.policy.*;
import seok.springBank.repository.policyRepository.PolicyRepositoryV2;

@Transactional(isolation = Isolation.SERIALIZABLE)
@RequiredArgsConstructor
@Service
public class PolicyService {
    private final PolicyRepositoryV2 policyRepository;

    public Policy makePolicy(PolicySaveForm policySaveForm){
        Policy policy=null;
        if (policySaveForm.getDtype()=="CHECKING_POLICY"){
            policy = CheckingPolicy.createCheckingPolicy(policySaveForm);
        }
        else if(policySaveForm.getDtype()=="COMMODITY_POLICY"){
            policy = CommodityPolicy.createCommodityPolicy(policySaveForm);
        }
        else if(policySaveForm.getDtype()=="LOAN_POLICY"){
            policy = LoanPolicy.createLoanPolicy(policySaveForm);
        }
        policyRepository.save(policy);

        return policy;
    }

    public Policy findPolicyByName(String name){
        return policyRepository.findPolicyByPolicyName(name)
                .orElseThrow(()-> new IllegalArgumentException("Invalid Access"));

    }
}
