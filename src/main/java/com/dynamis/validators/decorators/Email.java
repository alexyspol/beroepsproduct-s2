package com.dynamis.validators.decorators;

import com.dynamis.validators.IValidator;

public class Email extends BaseValidatorDecorator {

    public Email(IValidator validator) {
        super(validator);
    }

    @Override
    public boolean isValid() {
        return super.isValid() && ((String) getValue()).matches("^[\\w._-]+@[\\w.-]+\\.[A-Za-z]{2,}$");
    }
}
