package com.dynamis.validators;

public class EmptyAllowed extends BaseValidatorDecorator {

    public EmptyAllowed(Validator validator) {
        super(validator);
    }

    @Override
    public boolean isValid() {
        return validator.isValid() || ((String) getValue()).isEmpty();
    }

}
