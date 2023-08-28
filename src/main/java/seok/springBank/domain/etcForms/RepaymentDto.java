package seok.springBank.domain.etcForms;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RepaymentDto {
    Long money;
    String targetCheckingAccount;
}
