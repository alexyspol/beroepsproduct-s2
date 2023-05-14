package com.dynamis.validators;

public abstract class BaseValidatorDecorator implements Validator {

    protected Validator validator;

    public BaseValidatorDecorator(Validator validator) {
        this.validator = validator;
    }

    public boolean isValid() {
        return validator.isValid();
    }

    public void setValue(Object value) {
        validator.setValue(value);
    }

    public Object getValue() {
        return validator.getValue();
    }

}
