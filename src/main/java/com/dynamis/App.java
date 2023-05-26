package com.dynamis;

import java.io.BufferedInputStream;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.dynamis.controllers.*;

public class App {

    private static String url = "jdbc:sqlite:hackathon.db";

    public boolean quit = false;
    private List<Controller> controllers = new ArrayList<>();
    private Scanner scanner = new Scanner(new BufferedInputStream(System.in));

    public static void main(String[] args) {
        App app = new App();

        while(!app.quit) {
            app.run();
        }
    }

    public App() {
        try(Statement statement = DriverManager.getConnection(url).createStatement()) {
            statement.executeUpdate("""
                DROP TABLE IF EXISTS users;
                DROP TABLE IF EXISTS teams;
                DROP TABLE IF EXISTS contact_info;
            """);
            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS users (
                    student_id TEXT PRIMARY KEY,
                    first_name TEXT,
                    last_name TEXT,
                    dob TEXT,
                    skill TEXT,
                    team_id INTEGER REFERENCES teams(team_id) ON DELETE SET NULL
                );

                CREATE TABLE IF NOT EXISTS teams (
                    team_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    team_name TEXT
                );

                CREATE TABLE IF NOT EXISTS contact_info (
                    student_id TEXT PRIMARY KEY REFERENCES users(student_id),
                    phone TEXT,
                    email TEXT,
                    residence TEXT
                );
            """);
            statement.executeUpdate("""
                INSERT INTO users (student_id, first_name, last_name, dob, team_id, skill) VALUES
                ('001', 'John', 'Doe', '1995-07-01', 1, 'Java'),
                ('002', 'Jane', 'Smith', '1996-10-05', 1, 'Python'),
                ('003', 'Bob', 'Johnson', '1997-01-15', 2, 'JavaScript'),
                ('004', 'Sarah', 'Lee', '1998-04-30', 2, 'C#'),
                ('005', 'David', 'Kim', '1995-09-12', 3, 'PHP'),
                ('006', 'Emily', 'Garcia', '1996-12-25', 3, 'Ruby'),
                ('007', 'Jason', 'Nguyen', '1997-05-18', 4, 'Swift'),
                ('008', 'Megan', 'Wong', '1998-08-22', 4, 'C++');

                INSERT INTO teams (team_name) VALUES
                ('Team A'),
                ('Team B'),
                ('Team C'),
                ('Team D');

                INSERT INTO contact_info (student_id, phone, email, residence) VALUES
                ('001', '555-1234', 'johndoe@example.com', '123 Main St, Anytown USA'),
                ('002', '555-5678', 'janesmith@example.com', '456 Elm St, Anytown USA'),
                ('003', '555-9876', 'bobjohnson@example.com', '789 Oak St, Anytown USA'),
                ('004', '555-4321', 'sarahlee@example.com', '321 Pine St, Anytown USA'),
                ('005', '555-2468', 'davidkim@example.com', '654 Maple St, Anytown USA'),
                ('006', '555-3690', 'emilygarcia@example.com', '987 Cedar St, Anytown USA'),
                ('007', '555-1357', 'jasonnguyen@example.com', '246 Birch St, Anytown USA'),
                ('008', '555-5793', 'meganwong@example.com', '135 Walnut St, Anytown USA');
            """);
        }
        catch(SQLException e) {
            e.printStackTrace();
        }

        this.controllers.add(new DisplayUsersController("Display list of users", url));
        this.controllers.add(new DisplayTeamsController("Display list of teams", url));
        this.controllers.add(new CreateUserController("Create user", url));
        this.controllers.add(new CreateTeamController("Create team", url));
        this.controllers.add(new EditUserController("Edit user", url));
        this.controllers.add(new EditTeamController("Edit team", url));
        this.controllers.add(new DeleteUserController("Delete user", url));
        this.controllers.add(new DeleteTeamController("Delete team", url));
        this.controllers.add(new ExitApplicationController("Exit application", this));
    }

    public void run() {

        System.out.println("\nChoose an option:");
        for(int i = 0; i < controllers.size(); i++) {
            System.out.printf("%d. %s\n", i+1, controllers.get(i));
        }

        int selected = filter(scanner.nextLine().trim());

        if(!(1 <= selected && selected <= controllers.size())) {
            System.out.println("> Please enter a number between 1 and " + controllers.size());
            return;
        }

        controllers.get(selected-1).run();
    }

    // TODO Make this better
    private static int filter(String str) {
        int result;

        try {
            result = Integer.parseInt(str);
        }
        catch(NumberFormatException e) {
            result = 0;
        }

        return result;
    }

}
