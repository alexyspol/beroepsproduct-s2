package com.dynamis.validators;

import java.time.LocalDate;

public class DateString extends BaseValidatorDecorator {

    public DateString(Validator validator) {
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
