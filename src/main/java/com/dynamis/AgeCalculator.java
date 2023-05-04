package com.dynamis;

import java.time.LocalDate;
import java.time.Period;

public class AgeCalculator {
    public static int calculate(String dob) {
        LocalDate birthDate = LocalDate.parse(dob);
        LocalDate currentDate = LocalDate.now();
        return Period.between(birthDate, currentDate).getYears();
    }
}
