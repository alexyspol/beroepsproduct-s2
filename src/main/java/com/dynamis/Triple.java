package com.dynamis;

import com.dynamis.validators.Validator;

public class Triple {
    String request;
    String columnName;
    Validator validator;

    public Triple(String request, String columnName, Validator validator) {
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

    public Validator getValidator() {
        return validator;
    }
}
