package com.dynamis.validators;

public interface Validator {
    public Object getValue();
    public void setValue(Object value);
    public boolean isValid();
}
