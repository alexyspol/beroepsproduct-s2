package com.dynamis.views;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class DeleteTeamView {

    private Scanner scanner;

    public DeleteTeamView(Scanner scanner) {
        this.scanner = scanner;
    }

    public int pickOne(List<Map<String, Object>> teams) {
        int selected = 0;
        boolean isSelectionValid = false;

        do {
            System.out.println("\nDelete team:");
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

    public void success() {
        System.out.println("\n> Team deleted");
    }

}
