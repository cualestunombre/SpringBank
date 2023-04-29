package seok.springBank.validator;

import seok.springBank.constraints.AlphaNumericWithNumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class AlphaNumericWithNumberValidator implements ConstraintValidator<AlphaNumericWithNumber, String> {

    private static final Pattern ALPHA_NUMERIC_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[a-zA-Z])[a-zA-Z0-9]+$");

    @Override
    public void initialize(AlphaNumericWithNumber constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true;
        }
        return ALPHA_NUMERIC_PATTERN.matcher(value).matches();
    }
}