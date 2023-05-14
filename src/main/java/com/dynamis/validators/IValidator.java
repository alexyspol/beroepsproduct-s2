package com.dynamis.validators;

public interface IValidator {
    public Object getValue();
    public void setValue(Object value);
    public boolean isValid();
}
