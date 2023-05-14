package com.dynamis.validators;

public class Email extends BaseValidatorDecorator {

    public Email(Validator validator) {
        super(validator);
    }

    @Override
    public boolean isValid() {
        return super.isValid() && ((String) getValue()).matches("^[\\w._-]+@[\\w.-]+\\.[A-Za-z]{2,}$");
    }
}
