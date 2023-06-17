package seok.springBank.domain.etcForms;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class DepositForm {
    @NotEmpty
    String accountNumber;
    @Min(1)@Max(999999999)
    Long  balance;

}
