package seok.springBank.repository.accountRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import seok.springBank.domain.account.Account;
import seok.springBank.domain.account.AccountSaveForm;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class AccountRepository {
    private final EntityManager em;

    public Account saveAccount(Account account){
        em.persist(account);
        return account;
    }

    public List<Account> findByAccountNumber(String accountNumber){
        return em.createQuery("select a from Account a where a.accountNumber=:accountNumber", Account.class)
                .setParameter("accountNumber",accountNumber)
                .getResultList();
    }

}
