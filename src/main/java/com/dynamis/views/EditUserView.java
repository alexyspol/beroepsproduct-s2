package com.dynamis.views;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class EditUserView {

    private Scanner scanner;

    public EditUserView(Scanner scanner) {
        this.scanner = scanner;
    }

    public int pickOne(List<Map<String, Object>> users) {
        int selected = 0;
        boolean isSelectionValid = false;

        do {
            System.out.println("Edit user:");
            for(int i = 0; i < users.size(); i++) {
                Map<String, Object> user = users.get(i);
                System.out.printf("%d. %s %s\n", i+1, user.get("first_name"), user.get("last_name"));
            }

            String input = scanner.nextLine().trim();

            try {
                selected = Integer.parseInt(input);
            }
            catch(NumberFormatException e) {
                e.printStackTrace();
            }

            isSelectionValid = (1 <= selected && selected <= users.size());

        } while(!isSelectionValid);

        return selected - 1;
    }

    public boolean allowEditing(String studentID, String firstName, String lastName) {
        boolean isAllowed = false;
        int chances = 3;

        for(int i = 1; i <= chances; i++) {
            System.out.printf("Enter %s %s's Student ID (%d/%d): ", firstName, lastName, i, chances);
            String answer = scanner.nextLine().trim();
            if(answer.equals(studentID)) {
                isAllowed = true;
                break;
            }
        }
        return isAllowed;
    }

    public String editFirstName(String oldFirstName) {
        System.out.printf("First name (%s): ", oldFirstName);
        return scanner.nextLine().trim();
    }

    public String editLastName(String oldLastName) {
        System.out.printf("Last name (%s): ", oldLastName);
        return scanner.nextLine().trim();
    }

    public String editStudentID(String oldStudentID) {
        String studentID;

        do {
            System.out.printf("Student ID (%s): ", oldStudentID);
            studentID = scanner.nextLine().trim();

        } while(!studentID.isEmpty() && !studentID.matches("^[A-Za-z]{2}/\\d{4}/\\d{3}$"));

        return studentID.toUpperCase();
    }

    public String editDateOfBirth(String oldDob) {
        String dob;
        boolean isValidDateString;

        do {
            System.out.printf("Date of birth (%s): ", oldDob);
            dob = scanner.nextLine().trim();
            try {
                LocalDate.parse(dob); // will throw if invalid
                isValidDateString = true;
            }
            catch(Exception e) {
                isValidDateString = false;
            }

        } while(!dob.isEmpty() && !isValidDateString);

        return dob;
    }

    public String editSkill(String oldSkill) {
        System.out.printf("Skill (%s): ", oldSkill);
        return scanner.nextLine().trim();
    }

    public int changeTeam(int currentTeamIndex, List<Map<String, Object>> existingTeams) {
        int selected;
        boolean isSelectedValid = false;

        do {
            System.out.println("\nChange team:");
            for (int i = 0; i < existingTeams.size(); i++) {
                System.out.printf("%d. %s", i+1, existingTeams.get(i).get("team_name"));

                if(i == currentTeamIndex) {
                    System.out.println(" (current)");
                }
                else {
                    System.out.println();
                }
            }
            System.out.printf("%d. %s\n", existingTeams.size() + 1, "[Skip]");

            selected = parseInt(scanner.nextLine().trim());
            isSelectedValid = (1 <= selected && selected <= existingTeams.size() + 1);

            if(!isSelectedValid) {
                System.out.printf("> Please enter a number between 1 and %d\n", existingTeams.size() + 1);
            }
    
        } while(!isSelectedValid);

        if(selected == existingTeams.size() + 1) {
            return (int) existingTeams.get(currentTeamIndex).get("team_id");
        }
        else {
            return (int) existingTeams.get(selected - 1).get("team_id");
        }
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

    public String editPhoneNumber(String oldPhoneNumber) {
        String phoneNumber;

        do {
            System.out.printf("Phone number (%s): ", oldPhoneNumber);
            phoneNumber = scanner.nextLine().trim();

        } while(!phoneNumber.isEmpty() && !phoneNumber.matches("0\\d{3}-\\d{4}|0\\d{7}|\\d{3}-\\d{4}|\\d{7}"));

        return phoneNumber;
    }

    public String editEmail(String oldEmail) {
        String email;

        do {
            System.out.printf("E-mail (%s): ", oldEmail);
            email = scanner.nextLine().trim();

        } while(!email.isEmpty() && !email.matches("^[\\w._-]+@[\\w.-]+\\.[A-Za-z]{2,}$"));

        return email;
    }

    public String editResidence(String oldResidence) {
        System.out.printf("Residence (%s): ", oldResidence);
        return scanner.nextLine().trim();
    }

    public void success() {
        System.out.println("\n> Edits applied");
    }

}
