package com.dynamis.views;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class EditTeamView {

    private Scanner scanner;

    public EditTeamView(Scanner scanner) {
        this.scanner = scanner;
    }

    public int pickOne(List<Map<String, Object>> teams) {
        int selected = 0;
        boolean isSelectionValid = false;

        do {
            System.out.println("\nEdit team:");
            for(int i = 0; i < teams.size(); i++) {
                Map<String, Object> team = teams.get(i);
                System.out.printf("%d. %s\n", i+1, team.get("team_name"));
            }

            selected = parseInt(scanner.nextLine().trim());

            isSelectionValid = (1 <= selected && selected <= teams.size());

        } while(!isSelectionValid);

        return selected - 1;
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

    public String promptForNewTeamName(String currentName, String[] existingNames) {
        String teamName = "";
        int chances = 3;
        System.out.println();

        for(int i = 1; i <= chances; i++) {
            System.out.printf("Change name (%s) (%d/%d): ", currentName, i, chances);
            teamName = scanner.nextLine().trim();
            boolean exists = false;

            if(teamName.isEmpty()) {
                continue;
            }

            for (String name : existingNames) {
                if (name.toLowerCase().equals(teamName.toLowerCase())) {
                    exists = true;
                    System.out.printf("> \"%s\" already exists\n", teamName);
                    break;
                }
            }

            if(!exists) {
                break;
            }
        }
        return teamName;
    }

    public void success() {
        System.out.println("\n> Team name changed");
    }

    public void showNoChanges() {
        System.out.println("\n> No changes made");
    }

}
