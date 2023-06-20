package seok.springBank.domain.etcForms;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ErrorResult {
    private final String message;
    private final Integer code;


}
