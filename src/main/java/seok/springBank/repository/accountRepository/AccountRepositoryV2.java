package seok.springBank.repository.accountRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import seok.springBank.domain.account.Account;
import seok.springBank.domain.account.CheckingAccount;
import seok.springBank.domain.member.Member;

import java.util.List;

public interface AccountRepositoryV2 extends JpaRepository<Account,Long> {
    List<Account> findByAccountNumber(String accountNumber);

    // 회원이 가진 checkingAccount의 수
    @Query("select count(*) from CheckingAccount a  where a.member=:member")
    Long countChecking(@Param("member")Member member);

    // 회원이 가진 commodityAccount의 수

    @Query("select count(*) from CommodityAccount a where a.member=:member")
    Long countCommodity(@Param("member")Member member);

    @Query("select c from CheckingAccount c join c.member where c.member.id=:id")
    List<CheckingAccount> findCheckingAccountById(@Param("id")Long id);

    @Query("select a from Account a join a.member m where m.id=:memberId and a.id=:accountId")
    Account isMyAccount(@Param("memberId")Long memberId,@Param("accountId")Long accountId);


}
