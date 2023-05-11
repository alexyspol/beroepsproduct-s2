package com.dynamis.validators;

public class NoneValidator implements Validator {

    @Override
    public boolean isValid(String anyValue) {
        return true;
    }
}
