package seok.springBank.domain.transfer;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class TransferForm {
    @NotNull
    String myAccountNumber;
    @NotNull
    String targetAccountNumber;
    @NotNull @Min(value = 1)
    Long amount;


}
