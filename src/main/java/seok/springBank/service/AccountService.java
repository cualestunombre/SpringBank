package seok.springBank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import seok.springBank.domain.account.*;
import seok.springBank.domain.member.Member;
import seok.springBank.domain.policy.CheckingPolicy;
import seok.springBank.domain.policy.CommodityPolicy;
import seok.springBank.domain.policy.Policy;
import seok.springBank.repository.accountRepository.AccountRepository;
import seok.springBank.repository.memberRepository.MemberRepository;
import seok.springBank.repository.policyRepository.PolicyRepository;
import seok.springBank.utility.AccountNumberGenerator;

@Transactional(isolation = Isolation.SERIALIZABLE)
@RequiredArgsConstructor
@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final MemberRepository memberRepository;
    private final PolicyRepository policyRepository;


    public Account makeAccount(AccountSaveForm accountSaveForm, Long memberId, Long policyId) {

        Account account=null;
        Member member = memberRepository.findById(memberId);
        Policy policy = policyRepository.findById(policyId);
        String dtype = accountSaveForm.getDtype();
        String accountNumber;
        String name = accountSaveForm.getName();;
       /*
        적금계좌 (00), 입출금계좌(01), 상품계좌(10), 대출계좌(11)
         */

        if (dtype.equals("CHECKING_ACCOUNT")) {
            if (!(policy instanceof CheckingPolicy)) {
                throw new IllegalArgumentException("Invalid access");
            }


            while (true) {
                accountNumber = "01" + AccountNumberGenerator.generateRandomNumber();
                if (accountRepository.findByAccountNumber(accountNumber).isEmpty()) {
                    break;
                }
            }

            account = CheckingAccount.createCheckingAccount(name,accountNumber, policy, member);
        }

        if (dtype.equals("COMMODITY_ACCOUNT")){
            if(!(policy instanceof CommodityPolicy)){
                throw new IllegalArgumentException("Invalid access");
            }
            while(true){
                accountNumber = "10"+ AccountNumberGenerator.generateRandomNumber();
                if (accountRepository.findByAccountNumber(accountNumber).isEmpty()){
                    break;
                }
            }

            account = CommodityAccount.createCommodityAccount(accountNumber,policy,member);

        }

        accountRepository.saveAccount(account);



        return account;
    }
}
