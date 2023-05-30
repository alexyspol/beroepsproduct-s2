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
import com.dynamis.views.EditUserView;

public class EditUserController implements Controller {

    private String description;
    private EditUserView view = new EditUserView(new Scanner(new BufferedInputStream(System.in)));
    private Users users;
    private Teams teams;
    private ContactInfo contacts;

    public EditUserController(String description, String url) {
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

    @Override
    public void run() {
        List<Map<String, Object>> everyone = users.readAll();
        int selected = view.pickOne(everyone);
        Map<String, Object> selectedUser = everyone.get(selected);

        boolean isEditingAllowed = view.allowEditing(
            (String) selectedUser.get("student_id"),
            (String) selectedUser.get("first_name"),
            (String) selectedUser.get("last_name")
        );

        if(isEditingAllowed) {
            Map<String, Object> selectedTeam = teams.read(selectedUser.get("team_id"));
            Map<String, Object> selectedContact = contacts.read(selectedUser.get("student_id"));

            Map<String, Object> newUserData = new HashMap<>();
            newUserData.put("first_name", view.editFirstName((String) selectedUser.get("first_name")));
            newUserData.put("last_name", view.editLastName((String) selectedUser.get("last_name")));
            // newUserData.put("student_id", view.editStudentID((String) selectedUser.get("student_id")));
            newUserData.put("dob", view.editDateOfBirth((String) selectedUser.get("dob")));
            newUserData.put("skill", view.editSkill((String) selectedUser.get("skill")));
            newUserData.put("team_id", view.changeTeam((String) selectedTeam.get("team_name"), teams.readAll()));
            newUserData = mergeData(selectedUser, newUserData);

            Map<String, Object> newContactData = new HashMap<>();
            newContactData.put("phone", view.editPhoneNumber((String) selectedContact.get("phone")));
            newContactData.put("email", view.editEmail((String) selectedContact.get("email")));
            newContactData.put("residence", view.editResidence((String) selectedContact.get("residence")));
            newContactData = mergeData(selectedContact, newContactData);

            users.update(selectedUser.get("student_id"), newUserData);
            contacts.update(selectedContact.get("student_id"), newContactData);

            if(newUserData.values().size() + newContactData.values().size() == 0) {
                view.noEditsMade();
            }
            else {
                view.success();
            }
        }
    }

    public static Map<String, Object> mergeData(Map<String, Object> currentData, Map<String, Object> newData) {
        Map<String, Object> merged = new HashMap<>();
        for (String key : newData.keySet()) {
            Object newValue = newData.get(key);
            Object currentValue = currentData.get(key);
    
            if (newValue instanceof String && !((String) newValue).isEmpty() && !newValue.equals(currentValue)) {
                merged.put(key, newValue);
            } else if (newValue instanceof Integer && !newValue.equals(currentValue)) {
                merged.put(key, newValue);
            }
        }
        return merged;
    }

    public String toString() {
        return description;
    }
}
