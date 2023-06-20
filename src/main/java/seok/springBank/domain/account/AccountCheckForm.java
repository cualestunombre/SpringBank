package seok.springBank.domain.account;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class AccountCheckForm {
    private final String name;
    private final String accountNumber;

    public static AccountCheckForm makeAccountCheckForm(Account account){
        return new AccountCheckForm(account.getMember().getName(), account.getAccountNumber());

    }
}
