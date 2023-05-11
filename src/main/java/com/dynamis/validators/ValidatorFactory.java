package com.dynamis.validators;

public class ValidatorFactory {
    public static Validator create(String columnName) {
        Validator validator = null;

        switch(columnName) {
            case "student_id":
                validator = new StudentIdValidator();
                break;

            case "dob":
                validator = new DateValidator();
                break;

            case "phone":
                validator = new PhoneNumberValidator();
                break;

            case "email":
                validator = new EmailValidator();
                break;

            default:
                validator = new NoneValidator();
        }

        return validator;
    }
}
