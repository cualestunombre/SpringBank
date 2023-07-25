package seok.springBank.domain.account;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class LoanAccountSaveForm {
    @NotNull
    @Min(1)
    private Long policyId;
    @NotNull
    @Min(1)
    private Long amount;
    @NotNull
    @Min(1)
    private Long duration;
}
