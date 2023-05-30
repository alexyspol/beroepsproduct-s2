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

        } while(studentID.isEmpty() || !studentID.matches("^[A-Za-z]{2,3}/\\d{4}/\\d{3}$"));

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
        int selected;

        do {
            System.out.println("\nJoin team: ");
            for(int i = 0; i < existingTeams.size(); i++) {
                Map<String, Object> team = existingTeams.get(i);
                System.out.printf("%d. %s\n", i+1, team.get("team_name"));
            }

            selected = parseInt(scanner.nextLine().trim());

            if(!(1 <= selected && selected <= existingTeams.size())) {
                System.out.println("> Please enter a number between 1 and " + existingTeams.size());
            }

        } while(!(1 <= selected && selected <= existingTeams.size()));

        return (int) existingTeams.get(selected - 1).get("team_id");
    }

    private static int parseInt(String str) {
        int result;

        try {
            result = Integer.parseInt(str);
        }
        catch(NumberFormatException e) {
            result = 0;
        }

        return result;
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

        return email.toLowerCase();
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

}
