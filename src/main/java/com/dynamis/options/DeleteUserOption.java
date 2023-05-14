package com.dynamis.options;

import java.io.BufferedInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.dynamis.App;
import com.dynamis.SQLFileReader;
import com.dynamis.validators.InRange;
import com.dynamis.validators.IntegerValidator;
import com.dynamis.validators.Validator;

public class DeleteUserOption implements Option {

    @Override
    public void run(App app) throws SQLException {

        Scanner s = new Scanner(new BufferedInputStream(System.in));
        Map<String, String> sql = SQLFileReader.readSQLFile("delete_user.sql");

        try(Connection c = DriverManager.getConnection("jdbc:sqlite:hackathon.db");
            PreparedStatement selectAllUsers = c.prepareStatement(sql.get("select_all_users"));
            PreparedStatement deleteUser = c.prepareStatement(sql.get("delete_single_user"));
            PreparedStatement deleteContactInfo = c.prepareStatement(sql.get("delete_contact_info"));
            ResultSet rs = selectAllUsers.executeQuery()) {

            List<Map<String, Object>> users = new ArrayList<>();
            Map<String, Object> selectedUser = null;

            while(rs.next()) {
                Map<String, Object> user = new HashMap<>();
                user.put("student_id", rs.getString("student_id"));
                user.put("first_name", rs.getString("first_name"));
                user.put("last_name", rs.getString("last_name"));
                users.add(user);
            }

            // Ask who needs to be removed

            Validator validator = new InRange((new IntegerValidator()), 1, users.size());

            do {
                System.out.println("\nRemove:");
                for(int i = 0; i < users.size(); i++) {
                    Map<String, Object> user = users.get(i);
                    System.out.printf("%d. %s %s\n", i+1, user.get("first_name"), user.get("last_name"));
                }
                validator.setValue(s.nextInt());
                s.nextLine(); // consume the previous newline character

            } while(!validator.isValid());

            selectedUser = users.get((int) validator.getValue() - 1);

            // Perform changes

            deleteUser.setString(1, (String) selectedUser.get("student_id"));
            deleteUser.executeUpdate();

            deleteContactInfo.setString(1, (String) selectedUser.get("student_id"));
            deleteContactInfo.executeUpdate();

            // Print information

            System.out.printf("\n> User deleted: %s %s (%s)\n\n", selectedUser.get("first_name"), selectedUser.get("last_name"), selectedUser.get("student_id"));
        }
    }

    @Override
    public String toString() {
        return "Delete user";
    }

}
