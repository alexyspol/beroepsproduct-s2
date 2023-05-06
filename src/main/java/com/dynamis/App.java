package com.dynamis;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.dynamis.options.CreateGroupOption;
import com.dynamis.options.CreateUserOption;
import com.dynamis.options.DeleteTeamOption;
import com.dynamis.options.DeleteUserOption;
import com.dynamis.options.DisplayTeamsOption;
import com.dynamis.options.DisplayUsersOption;
import com.dynamis.options.EditGroupOption;
import com.dynamis.options.EditUserByIdOption;
import com.dynamis.options.ExitApplicationOption;
import com.dynamis.options.Option;

public class App implements AutoCloseable {

    private Scanner scanner;
    private Connection connection;
    private String url = "jdbc:sqlite:hackathon.db";

    private List<Option> options = new ArrayList<>();
    private boolean userWantsToExit = false;

    private String[] sqlStatements = {
        """
            CREATE TABLE IF NOT EXISTS users (
                student_id TEXT PRIMARY KEY,
                first_name TEXT,
                last_name TEXT,
                dob TEXT,
                team_id INTEGER REFERENCES teams(id)
            );
        """,
        """
            CREATE TABLE IF NOT EXISTS teams (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                team_name TEXT
            );
        """,
        """
            CREATE TABLE IF NOT EXISTS contact_info (
                student_id TEXT PRIMARY KEY REFERENCES users(student_id),
                phone TEXT,
                email TEXT,
                residence TEXT,
                skill TEXT
            );
        """
    };

    private String[] dummyData = {
        """
            INSERT INTO users (student_id, first_name, last_name, dob, team_id) VALUES
            ('001', 'John', 'Doe', '1995-07-01', 1),
            ('002', 'Jane', 'Smith', '1996-10-05', 1),
            ('003', 'Bob', 'Johnson', '1997-01-15', 2),
            ('004', 'Sarah', 'Lee', '1998-04-30', 2),
            ('005', 'David', 'Kim', '1995-09-12', 3),
            ('006', 'Emily', 'Garcia', '1996-12-25', 3),
            ('007', 'Jason', 'Nguyen', '1997-05-18', 4),
            ('008', 'Megan', 'Wong', '1998-08-22', 4);
        """,
        """
            INSERT INTO teams (team_name) VALUES
            ('Team A'),
            ('Team B'),
            ('Team C'),
            ('Team D');
        """,
        """
            INSERT INTO contact_info (student_id, phone, email, residence, skill) VALUES
            ('001', '555-1234', 'johndoe@example.com', '123 Main St, Anytown USA', 'Java'),
            ('002', '555-5678', 'janesmith@example.com', '456 Elm St, Anytown USA', 'Python'),
            ('003', '555-9876', 'bobjohnson@example.com', '789 Oak St, Anytown USA', 'JavaScript'),
            ('004', '555-4321', 'sarahlee@example.com', '321 Pine St, Anytown USA', 'C#'),
            ('005', '555-2468', 'davidkim@example.com', '654 Maple St, Anytown USA', 'PHP'),
            ('006', '555-3690', 'emilygarcia@example.com', '987 Cedar St, Anytown USA', 'Ruby'),
            ('007', '555-1357', 'jasonnguyen@example.com', '246 Birch St, Anytown USA', 'Swift'),
            ('008', '555-5793', 'meganwong@example.com', '135 Walnut St, Anytown USA', 'C++');
        """
    };

    public static void main(String[] args) {

        try(App app = new App()) {
            while(!app.exitApplication()) {
                app.run();
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public App() throws SQLException, IOException {

        this.scanner = new Scanner(new BufferedInputStream(System.in));
        this.connection = DriverManager.getConnection(this.url);
        Statement statement = this.connection.createStatement();

        // 1. Create tables

        for(String sql : sqlStatements) {
            statement.executeUpdate(sql);
        }

        // 2. Insert dummy data

        for(String sql : dummyData) {
            statement.executeUpdate(sql);
        }

        // 3. Add options
        
        this.addOption(new DisplayUsersOption());
        this.addOption(new DisplayTeamsOption());
        this.addOption(new EditUserByIdOption());
        this.addOption(new EditGroupOption());
        this.addOption(new CreateUserOption());
        this.addOption(new CreateGroupOption());
        this.addOption(new DeleteUserOption());
        this.addOption(new DeleteTeamOption());
        this.addOption(new ExitApplicationOption());
    }

    public void run() throws SQLException, IOException {
        System.out.println("1. Display list of users");
        System.out.println("2. Display list of groups");
        System.out.println("3. Edit user by id");
        System.out.println("4. Edit group");
        System.out.println("5. Create user");
        System.out.println("6. Create group");
        System.out.println("7. Delete user");
        System.out.println("8. Delete group");
        System.out.println("9. Exit application");

        int answer = this.scanner.nextInt();

        if(!(1 <= answer && answer <= this.options.size())) {
            throw new IllegalArgumentException("Your answer needs to be between 1 and " + (this.options.size()));
        }

        this.options.get(answer - 1).run(this);
    }

    public boolean exitApplication() {
        return this.userWantsToExit;
    }

    public void setUserWantsToExit(boolean userWantsToExit) {
        this.userWantsToExit = userWantsToExit;
    }

    public void addOption(Option option) {
        this.options.add(option);
    }
    
    @Override
    public void close() throws SQLException {
        this.scanner.close();
        this.connection.close();
    }

    public Connection getConnection() {
        return this.connection;
    }

    public Scanner getScanner() {
        return this.scanner;
    }
}
