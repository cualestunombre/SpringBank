package seok.springBank.domain.etcForms;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class InformationDto {
    private final Long total;
    private final Long amount;
    private final Long count;
}
