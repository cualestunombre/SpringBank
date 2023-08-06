package seok.springBank.domain.account;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
@Getter
@Setter
public class LoanTransferForm {
    //checkingAccountId:selectedChecking,amount:amount,loanName:selectedLoan
    @NotEmpty
    String loanName;
    @NotNull
    @Min(1)
    Long amount;

    @NotNull
    @Min(1)
    Long checkingAccountId;

}
