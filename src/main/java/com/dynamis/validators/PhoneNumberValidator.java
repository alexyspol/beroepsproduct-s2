package com.dynamis.validators;

public class PhoneNumberValidator implements Validator {

    @Override
    public boolean isValid(String phoneNumber) {
        return phoneNumber.matches("0\\d{3}-\\d{4}|0\\d{7}|\\d{3}-\\d{4}|\\d{7}");
    }
}
