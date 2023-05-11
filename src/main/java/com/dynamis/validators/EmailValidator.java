package com.dynamis.validators;

public class EmailValidator implements Validator {

    @Override
    public boolean isValid(String email) {
        return email.matches("^[\\w._-]+@[\\w.-]+\\.[A-Za-z]{2,}$");
    }
}
