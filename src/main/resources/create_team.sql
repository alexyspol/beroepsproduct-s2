-- select_all_teams_users
SELECT u.student_id, u.first_name, u.last_name, t.team_name
FROM users u
LEFT JOIN teams t ON u.team_id = t.id
ORDER BY t.team_name;

-- create_new_team
INSERT INTO teams (team_name)
VALUES (?);

-- add_users_to_team
UPDATE users
SET team_id = ?
WHERE student_id = ?;
