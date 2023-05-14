package com.dynamis.validators;

public class StudentID extends BaseValidatorDecorator {

    public StudentID(Validator validator) {
        super(validator);
    }

    @Override
    public boolean isValid() {
        return validator.isValid() && ((String) getValue()).matches("^[A-Za-z]{2}/\\d{4}/\\d{3}$");
    }

}
