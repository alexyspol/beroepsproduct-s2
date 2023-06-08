package com.dynamis.controllers;

import java.io.BufferedInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.dynamis.models.ContactInfo;
import com.dynamis.models.Teams;
import com.dynamis.models.Users;
import com.dynamis.views.CreateUserView;


public class CreateUserController implements Controller {

    private String description;
    private CreateUserView view = new CreateUserView(new Scanner(new BufferedInputStream(System.in)));
    private Users users;
    private Teams teams;
    private ContactInfo contacts;

    public CreateUserController(String description, String url) {
        this.description = description;
        try {
            Connection connection = DriverManager.getConnection(url);
            this.users = new Users(connection);
            this.teams = new Teams(connection);
            this.contacts = new ContactInfo(connection);
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }

    @Override public void run() {
        List<Map<String, Object>> existingTeams = teams.readAll();

        Map<String, Object> userData = new HashMap<>();
        userData.put("first_name", view.promptForFirstName());
        userData.put("last_name", view.promptForLastName());
        userData.put("student_id", view.promptForStudentID());
        userData.put("dob", view.promptForDateOfBirth());
        userData.put("skill", view.promptForSkill());

        Map<String, Object> contactData = new HashMap<>();
        contactData.put("student_id", userData.get("student_id"));
        contactData.put("phone", view.promptForPhoneNumber());
        contactData.put("email", view.promptForEmail());
        contactData.put("residence", view.promptForResidence());

        userData.put("team_id", view.promptForTeamName(existingTeams));
        
        users.create(userData);
        contacts.create(contactData);

        view.success();
    }

    @Override public String toString() {
        return this.description;
    }

}
