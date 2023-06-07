package seok.springBank.domain.policy;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PolicySaveForm {
    private Double interestRate;
    private String dtype;
    private String period;
    private String duration;
}
