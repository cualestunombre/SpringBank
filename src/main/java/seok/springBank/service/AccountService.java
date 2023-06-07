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
import seok.springBank.exceptions.account.AccountMoreThanFive;
import seok.springBank.repository.accountRepository.AccountRepository;
import seok.springBank.repository.accountRepository.AccountRepositoryV2;
import seok.springBank.repository.memberRepository.MemberRepository;
import seok.springBank.repository.memberRepository.MemberRepositoryV2;
import seok.springBank.repository.policyRepository.PolicyRepository;
import seok.springBank.repository.policyRepository.PolicyRepositoryV2;
import seok.springBank.utility.AccountNumberGenerator;

@Transactional(isolation = Isolation.SERIALIZABLE)
@RequiredArgsConstructor
@Service
public class AccountService {
    private final AccountRepositoryV2 accountRepository;
    private final MemberRepositoryV2 memberRepository;
    private final PolicyRepositoryV2 policyRepository;


    public Account makeAccount(AccountSaveForm accountSaveForm, Long memberId) throws AccountMoreThanFive{

        Account account=null;
        Member member = memberRepository.findById(memberId).orElseThrow(()->new IllegalArgumentException("Invalid Access"));
        Policy policy = policyRepository.findById(accountSaveForm.getPolicyId()).orElseThrow(()->new IllegalArgumentException("Invalid Access"));
        String dtype = accountSaveForm.getDtype();
        String accountNumber;
        String name = accountSaveForm.getName();
        System.out.println(dtype);

       /*
        적금계좌 (00), 입출금계좌(01), 상품계좌(10), 대출계좌(11)
         */

        if (dtype.equals("CHECKING_ACCOUNT")) {
            if (!(policy instanceof CheckingPolicy)) {
                throw new IllegalArgumentException("Invalid access");
            }

            checkMoreThan5(member,dtype);

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
            checkMoreThan5(member,dtype);

            while(true){
                accountNumber = "10"+ AccountNumberGenerator.generateRandomNumber();
                if (accountRepository.findByAccountNumber(accountNumber).isEmpty()){
                    break;
                }
            }

            account = CommodityAccount.createCommodityAccount(name,accountNumber,policy,member);

        }

        accountRepository.save(account);


        return account;
    }

    // 상품 계좌나 입출금 계좌가 5개 이상이면 예외를 발생시킴
    private void checkMoreThan5(Member member ,String dtype) throws AccountMoreThanFive{
        Long count = 0L;
        System.out.println(member.getId());
        if (dtype.equals("CHECKING_ACCOUNT")){
            count = accountRepository.countChecking(member);

        }
        else if (dtype.equals("COMMODITY_ACCOUNT")){
            count = accountRepository.countCommodity(member);

        }
        System.out.println(count);
        if (count>=5L){
            throw new AccountMoreThanFive();
        }
    }
}
