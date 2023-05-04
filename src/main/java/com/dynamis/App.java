package com.dynamis;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.dynamis.options.CreateGroupOption;
import com.dynamis.options.CreateUserOption;
import com.dynamis.options.DeleteGroupOption;
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

        String[] sqlStatements = SqlFileReader.read("src/main/resources/sqlite/create_tables.sql").split(";");

        for(String sql : sqlStatements) {
            statement.executeUpdate(sql + ';');
        }

        // 2. Insert dummy data

        sqlStatements = SqlFileReader.read("src/main/resources/sqlite/insert_dummy_data.sql").split(";");

        for(String sql : sqlStatements) {
            statement.executeUpdate(sql + ';');
        }

        // 3. Add options
        
        this.addOption(new DisplayUsersOption());
        this.addOption(new DisplayTeamsOption());
        this.addOption(new EditUserByIdOption());
        this.addOption(new EditGroupOption());
        this.addOption(new CreateUserOption());
        this.addOption(new CreateGroupOption());
        this.addOption(new DeleteUserOption());
        this.addOption(new DeleteGroupOption());
        this.addOption(new ExitApplicationOption());
    }

    public void run() throws SQLException {
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

        this.options.get(answer - 1).run(this.connection);
    }

    public boolean exitApplication() {
        return this.userWantsToExit;
    }

    public void addOption(Option option) {
        this.options.add(option);
    }
    
    @Override
    public void close() throws SQLException {
        this.scanner.close();
        this.connection.close();
    }

}
