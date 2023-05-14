package com.dynamis;

import com.dynamis.validators.IValidator;

public class Triplee {
    String request;
    String columnName;
    IValidator validator;

    public Triplee(String request, String columnName, IValidator validator) {
        this.request = request;
        this.columnName = columnName;
        this.validator = validator;
    }

    public String getRequest() {
        return request;
    }

    public String getColumnName() {
        return columnName;
    }

    public IValidator getValidator() {
        return validator;
    }
}
