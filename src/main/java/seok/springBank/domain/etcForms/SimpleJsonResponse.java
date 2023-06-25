package seok.springBank.domain.etcForms;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class SimpleJsonResponse {
    private final String message;
    private final Integer code;
}
