SELECT id
FROM teams
WHERE team_name = ?;

INSERT INTO teams (team_name)
VALUES (?);

INSERT INTO users (student_id, first_name, last_name, dob, team_id)
VALUES (?, ?, ?, ?, ?);

INSERT INTO contact_info (student_id, phone, email, residence, skill)
VALUES (?, ?, ?, ?, ?);
