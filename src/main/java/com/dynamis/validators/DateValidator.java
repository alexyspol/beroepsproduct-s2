package com.dynamis.validators;

import java.time.LocalDate;

public class DateValidator implements Validator {

    @Override
    public boolean isValid(String dateString) {
        boolean answer;
        try {
            LocalDate.parse(dateString); // will throw if invalid
            answer = true;
        }
        catch(Exception e) {
            answer = false;
        }
        return answer;
    }
}
