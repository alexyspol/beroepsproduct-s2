package com.dynamis.views;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class DeleteUserView {

    private Scanner scanner;

    public DeleteUserView(Scanner scanner) {
        this.scanner = scanner;
    }

    public int pickOne(List<Map<String, Object>> users) {
        int selected = 0;
        boolean isSelectionValid = false;

        do {
            System.out.println("\nDelete user:");
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

    public void success() {
        System.out.println("\n> User deleted");
    }

}
