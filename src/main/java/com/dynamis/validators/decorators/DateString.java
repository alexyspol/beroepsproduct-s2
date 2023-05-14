package com.dynamis.validators.decorators;

import java.time.LocalDate;

import com.dynamis.validators.IValidator;

public class DateString extends BaseValidatorDecorator {

    public DateString(IValidator validator) {
        super(validator);
    }

    @Override
    public boolean isValid() {
        boolean answer;

        try {
            LocalDate.parse((String) getValue()); // will throw if invalid
            answer = true;
        }
        catch(Exception e) {
            answer = false;
        }

        return super.isValid() && answer;
    }
}
