package com.dynamis.validators;

public class IsNotEmpty extends BaseValidatorDecorator {

    public IsNotEmpty(Validator validator) {
        super(validator);
    }

    @Override
    public boolean isValid() {
        return validator.isValid() && !((String) getValue()).isEmpty();
    }

}
