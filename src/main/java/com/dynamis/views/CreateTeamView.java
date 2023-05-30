package com.dynamis.views;

import java.util.Scanner;

public class CreateTeamView {

    private Scanner scanner;

    public CreateTeamView(Scanner scanner) {
        this.scanner = scanner;
    }

    public String promptForNewTeam(String[] existingNames) {
        String teamName = "";
        boolean isUniqueName = true;

        do {
            System.out.print("\nCreate new team: ");
            teamName = scanner.nextLine().trim();

            for (String name : existingNames) {
                if (name.toLowerCase().equals(teamName.toLowerCase())) {
                    isUniqueName = false;
                    System.out.printf("> \"%s\" already exists\n", teamName);
                    break;
                }
                else {
                    isUniqueName = true;
                }
            }

        } while(teamName.isEmpty() || !isUniqueName);

        return teamName;
    }

    public void success() {
        System.out.println("\n> Team created");
    }

}
