package com.dynamis;

import java.io.BufferedInputStream;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.dynamis.options.CreateTeamOption;
import com.dynamis.options.CreateUserOption;
import com.dynamis.options.DeleteTeamOption;
import com.dynamis.options.DeleteUserOption;
import com.dynamis.options.DisplayTeamsOption;
import com.dynamis.options.DisplayUsersOption;
import com.dynamis.options.EditTeamOption;
import com.dynamis.options.EditUserOption;
import com.dynamis.options.ExitApplicationOption;
import com.dynamis.options.Option;
import com.dynamis.validators.InRange;
import com.dynamis.validators.IntegerValidator;
import com.dynamis.validators.Validator;

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

            Map<String, String> schema = SQLFileReader.readSQLFile("schema.sql");
            Map<String, String> dummy = SQLFileReader.readSQLFile("dummy_data.sql");

            // Create tables

            stmt.executeUpdate(schema.get("create_users_table"));
            stmt.executeUpdate(schema.get("create_teams_table"));
            stmt.executeUpdate(schema.get("create_contact_info_table"));

            // Insert dummy data

            stmt.executeUpdate(dummy.get("dummy_users"));
            stmt.executeUpdate(dummy.get("dummy_teams"));
            stmt.executeUpdate(dummy.get("dummy_contact_info"));
        }

        // Add options

        this.addOption(new DisplayUsersOption());
        this.addOption(new DisplayTeamsOption());
        this.addOption(new EditUserOption());
        this.addOption(new EditTeamOption());
        this.addOption(new CreateUserOption());
        this.addOption(new CreateTeamOption());
        this.addOption(new DeleteUserOption());
        this.addOption(new DeleteTeamOption());
        this.addOption(new ExitApplicationOption());
    }

    public void run() throws SQLException {
        Scanner s = new Scanner(new BufferedInputStream(System.in));

        Validator validator = new InRange(new IntegerValidator(), 1, options.size());

        do {
            System.out.println("Choose an option:");
            for(int i = 0; i < this.options.size(); i++) {
                Option option = options.get(i);
                System.out.printf("%d. %s\n", i+1, option);
            }
            validator.setValue(s.nextInt());
            s.nextLine(); // consume the previous newline character

        } while(!validator.isValid());

        this.options.get((int) validator.getValue() - 1).run(this);
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
