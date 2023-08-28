package seok.springBank.domain.etcForms;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public class ApiDto {
    private final String apiKey;
    private final String password;
}
