SELECT u.student_id, u.first_name, u.last_name, u.team_id,
       t.team_name
FROM users u, teams t
WHERE u.team_id = t.id;

SELECT COUNT(u.student_id) AS num_team_members
FROM users u, teams t
WHERE u.team_id = ? AND t.id = ?;

DELETE FROM users
WHERE student_id = ?;

DELETE FROM teams
WHERE id = ?;

DELETE FROM contact_info
WHERE student_id = ?;
