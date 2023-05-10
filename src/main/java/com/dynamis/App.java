package com.dynamis;

import java.io.BufferedInputStream;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.dynamis.options.CreateTeamOption;
import com.dynamis.options.CreateUserOption;
import com.dynamis.options.DeleteTeamOption;
import com.dynamis.options.DeleteUserOption;
import com.dynamis.options.DisplayTeamsOption;
import com.dynamis.options.DisplayUsersOption;
import com.dynamis.options.EditTeamOption;
import com.dynamis.options.EditUserByIdOption;
import com.dynamis.options.ExitApplicationOption;
import com.dynamis.options.Option;

public class App {

    private List<Option> options = new ArrayList<>();
    private boolean userWantsToExit = false;

    public static void main(String[] args) throws SQLException {

        App app = new App();

        while(!app.exitApplication()) {
            app.run();
        }
    }

    public App() throws SQLException {

        File file = new File("hackathon.db");

        if (file.exists()) {
            file.delete();
        }

        try(Connection c = DriverManager.getConnection("jdbc:sqlite:hackathon.db");
            Statement stmt = c.createStatement()) {

            SQLFile sql = new SQLFile("schema.sql");
            SQLFile dummy = new SQLFile("dummy_data.sql");

            // Create tables

            stmt.executeUpdate(sql.nextStatement());
            stmt.executeUpdate(sql.nextStatement());
            stmt.executeUpdate(sql.nextStatement());

            // Insert dummy data

            stmt.executeUpdate(dummy.nextStatement());
            stmt.executeUpdate(dummy.nextStatement());
            stmt.executeUpdate(dummy.nextStatement());
        }

        // Add options

        this.addOption(new DisplayUsersOption());
        this.addOption(new DisplayTeamsOption());
        this.addOption(new EditUserByIdOption());
        this.addOption(new EditTeamOption());
        this.addOption(new CreateUserOption());
        this.addOption(new CreateTeamOption());
        this.addOption(new DeleteUserOption());
        this.addOption(new DeleteTeamOption());
        this.addOption(new ExitApplicationOption());
    }

    public void run() throws SQLException {
        Scanner s = new Scanner(new BufferedInputStream(System.in));

        System.out.println("Choose an option:");
        for(int i = 0; i < this.options.size(); i++) {
            Option option = options.get(i);
            System.out.printf("%d. %s\n", i+1, option);
        }

        int answer = s.nextInt();

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
}
