package seok.springBank.domain.account;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
public class AccountSaveForm {
    private String dtype;
    private Long policyId;
    @NotEmpty
    @Size(max=10)
    private String name;

}
