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

            String input = scanner.nextLine().trim();

            try {
                selected = Integer.parseInt(input);
            }
            catch(NumberFormatException e) {
                e.printStackTrace();
            }

            isSelectionValid = (1 <= selected && selected <= teams.size());

        } while(!isSelectionValid);

        return selected - 1;
    }

    public String promptForNewTeamName(String currentName, String[] existingNames) {
        String teamName = "";
        boolean isUniqueName = true;
        System.out.println();

        do {
            System.out.printf("Change name (%s): ", currentName);
            teamName = scanner.nextLine().trim();

            for (String name : existingNames) {
                if (name.toLowerCase().equals(teamName.toLowerCase())) {
                    isUniqueName = false;
                    break;
                }
                else {
                    isUniqueName = true;
                }
            }

        } while(!teamName.isEmpty() && !isUniqueName);

        return teamName;
    }

    public void success() {
        System.out.println("\n> Team name changed");
    }

    public void showNoChanges() {
        System.out.println("\n> No changes made");
    }

}
