-- select_team_by_id
SELECT id
FROM teams
WHERE team_name = ?;

-- create_new_user
INSERT INTO users (student_id, first_name, last_name, dob, team_id)
VALUES (?, ?, ?, ?, ?);

-- save_contact_info
INSERT INTO contact_info (student_id, phone, email, residence, skill)
VALUES (?, ?, ?, ?, ?);
