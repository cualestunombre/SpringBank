package seok.springBank.domain.member;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import seok.springBank.constraints.AlphaNumericWithNumber;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class MemberSaveForm {

    @Size(min=2,max=5)
    String name;

    @NotEmpty
    @Email
    String email;


    @AlphaNumericWithNumber
    @Size(min=6,max=18)
    String password;


    Boolean checked;

    @NotEmpty(message = "비어있을 수 없습니다")
    String emailToken;

}
