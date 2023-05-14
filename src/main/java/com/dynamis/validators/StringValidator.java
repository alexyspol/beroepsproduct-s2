package com.dynamis.validators;

public class StringValidator implements Validator {

    protected Object value;

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public boolean isValid() {
        return (value instanceof String);
    }

}
