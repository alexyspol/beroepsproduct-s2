package com.dynamis.validators.decorators;

import com.dynamis.validators.IValidator;

public abstract class BaseValidatorDecorator implements IValidator {

    protected IValidator validator;

    public BaseValidatorDecorator(IValidator validator) {
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
