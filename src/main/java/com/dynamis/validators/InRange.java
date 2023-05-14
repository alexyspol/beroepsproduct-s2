package com.dynamis.validators;

public class InRange extends BaseValidatorDecorator {

    private int max;
    private int min;

    public InRange(Validator validator, int min, int max) {
        super(validator);
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean isValid() {
        int val = (int) getValue();
        return validator.isValid() && (min <= val && val <= max);
    }

}
