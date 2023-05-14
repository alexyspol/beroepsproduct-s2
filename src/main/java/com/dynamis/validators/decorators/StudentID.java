package com.dynamis.validators.decorators;

import com.dynamis.validators.IValidator;

public class StudentID extends BaseValidatorDecorator {

    public StudentID(IValidator validator) {
        super(validator);
    }

    @Override
    public boolean isValid() {
        return validator.isValid() && ((String) getValue()).matches("^[A-Za-z]{2}/\\d{4}/\\d{3}$");
    }

}
