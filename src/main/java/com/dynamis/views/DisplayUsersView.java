package com.dynamis.views;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Map;

public class DisplayUsersView {

    public void show(List<Map<String, Object>> data) {
        for(int i = 0; i < data.size(); i++) {
            Map<String, Object> d = data.get(i);
            System.out.printf("""
            \n> %d. %s %s
                Age: %d
                Date of birth: %s
                Skill: %s
                Team: %s
                Phone number: %s
                E-mail address: %s
                Residence: %s
            """,
            i+1,
            d.get("first_name"),
            d.get("last_name"),
            calculateAge((String) d.get("dob")),
            d.get("dob"),
            d.get("skill"),
            d.get("team_name"),
            d.get("phone"),
            d.get("email"),
            d.get("residence"));
        }
    }

    public static int calculateAge(String dateString) {
        LocalDate currentDate = LocalDate.now();
        LocalDate birthDate = LocalDate.parse(dateString);

        Period period = Period.between(birthDate, currentDate);
        return period.getYears();
    }
    

}
