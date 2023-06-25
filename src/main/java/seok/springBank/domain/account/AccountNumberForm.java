package seok.springBank.domain.account;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Getter
@RequiredArgsConstructor
public class AccountNumberForm {
    private String targetAccountNumber;
    private String myAccountNumber;



}
