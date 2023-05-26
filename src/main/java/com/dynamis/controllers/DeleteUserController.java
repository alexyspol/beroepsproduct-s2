package com.dynamis.controllers;

import java.io.BufferedInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.dynamis.models.Users;
import com.dynamis.views.DeleteUserView;

public class DeleteUserController implements Controller {

    private String name;
    private DeleteUserView view = new DeleteUserView(new Scanner(new BufferedInputStream(System.in)));
    private Users users;

    public DeleteUserController(String name, String url) {
        this.name = name;
        try {
            Connection connection = DriverManager.getConnection(url);
            this.users = new Users(connection);
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }

    @Override public void run() {
        List<Map<String, Object>> everyone = users.readAll("student_id", "first_name", "last_name");
        int selected = view.pickOne(everyone);
        Map<String, Object> selectedUser = everyone.get(selected);

        users.delete(selectedUser.get("student_id"));
        view.success();
    }

    @Override public String toString() {
        return name;
    }

}
