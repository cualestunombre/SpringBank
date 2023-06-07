package seok.springBank.domain.account;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class AccountSaveForm {
    @NotEmpty
    private String dtype;
    private Long policyId;

    @Size(min=1,max=10)
    private String name;

}
