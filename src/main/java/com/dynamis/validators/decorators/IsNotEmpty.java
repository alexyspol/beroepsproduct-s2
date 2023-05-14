package com.dynamis.validators.decorators;

import com.dynamis.validators.IValidator;

public class IsNotEmpty extends BaseValidatorDecorator {

    public IsNotEmpty(IValidator validator) {
        super(validator);
    }

    @Override
    public boolean isValid() {
        return validator.isValid() && !((String) getValue()).isEmpty();
    }

}
