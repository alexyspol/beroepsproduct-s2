package com.dynamis.views;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class CreateUserView {

    private Scanner scanner;

    public CreateUserView(Scanner scanner) {
        this.scanner = scanner;
    }

    public String promptForFirstName() {
        String firstName;
        
        do {
            System.out.print("First name: ");
            firstName = scanner.nextLine().trim();
        } while(firstName.isEmpty());

        return firstName;
    }

    public String promptForLastName() {
        String lastName;
        
        do {
            System.out.print("Last name: ");
            lastName = scanner.nextLine().trim();

        } while(lastName.isEmpty());

        return lastName;
    }

    public String promptForStudentID() {
        String studentID;

        do {
            System.out.print("Student ID: ");
            studentID = scanner.nextLine().trim();

        } while(studentID.isEmpty() || !studentID.matches("^[A-Za-z]{2}/\\d{4}/\\d{3}$"));

        return studentID.toUpperCase();
    }

    public String promptForDateOfBirth() {
        String dob;
        boolean isValidDateString;

        do {
            System.out.print("Date of birth: ");
            dob = scanner.nextLine().trim();
            try {
                LocalDate.parse(dob); // will throw if invalid
                isValidDateString = true;
            }
            catch(Exception e) {
                isValidDateString = false;
            }

        } while(!isValidDateString);

        return dob;
    }

    public int promptForTeamName(List<Map<String, Object>> existingTeams) {
        String teamName;

        do {
            System.out.print("Join team: ");
            teamName = scanner.nextLine().trim();

        } while(teamName.isEmpty() || !isTeamNameExists(teamName, existingTeams));

        return getTeamIDFrom(teamName, existingTeams);
    }

    public String promptForPhoneNumber() {
        String phoneNumber;

        do {
            System.out.print("Phone number: ");
            phoneNumber = scanner.nextLine().trim();

        } while(!phoneNumber.isEmpty() && !phoneNumber.matches("0\\d{3}-\\d{4}|0\\d{7}|\\d{3}-\\d{4}|\\d{7}"));

        return phoneNumber;
    }

    public String promptForEmail() {
        String email;

        do {
            System.out.print("E-mail: ");
            email = scanner.nextLine().trim();

        } while(!email.isEmpty() && !email.matches("^[\\w._-]+@[\\w.-]+\\.[A-Za-z]{2,}$"));

        return email;
    }

    public String promptForResidence() {
        System.out.print("Residence: ");
        return scanner.nextLine().trim();
    }

    public String promptForSkill() {
        System.out.print("Skill: ");
        return scanner.nextLine().trim();
    }

    public void success() {
        System.out.println("\n> User created");
    }

    private int getTeamIDFrom(String teamName, List<Map<String, Object>> existingTeams) {
        int result = 0;

        for(Map<String, Object> team : existingTeams) {
            if(team.get("team_name").toString().equalsIgnoreCase(teamName)) {
                result = (int) team.get("team_id");
            }
        }

        return result;
    }

    private boolean isTeamNameExists(String teamName, List<Map<String, Object>> existingTeams) {
        boolean result = false;

        for(Map<String, Object> team : existingTeams) {
            if(team.get("team_name").toString().equalsIgnoreCase(teamName)) {
                result = true;
            }
        }

        return result;
    }

}
