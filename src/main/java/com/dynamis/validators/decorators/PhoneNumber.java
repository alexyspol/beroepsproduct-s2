package com.dynamis.validators.decorators;

import com.dynamis.validators.IValidator;

public class PhoneNumber extends BaseValidatorDecorator {

    public PhoneNumber(IValidator validator) {
        super(validator);
    }

    @Override
    public boolean isValid() {
        return validator.isValid() && ((String) getValue()).matches("0\\d{3}-\\d{4}|0\\d{7}|\\d{3}-\\d{4}|\\d{7}");
    }

}
