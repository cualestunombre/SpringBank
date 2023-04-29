package seok.springBank.constraints;

import seok.springBank.validator.AlphaNumericWithNumberValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AlphaNumericWithNumberValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface AlphaNumericWithNumber {

    String message() default "영문 대소문자와 숫자를 " +
            "" +
            "포함해야 합니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}