package seok.springBank.domain.etcForms;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public class OverdueDto {
    private final Long amount;
    private final Long count;

}
