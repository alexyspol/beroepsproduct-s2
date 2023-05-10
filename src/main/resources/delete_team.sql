
SELECT * FROM teams;

SELECT student_id, first_name, last_name
FROM users
WHERE team_id = ?;

DELETE FROM teams WHERE id = ?;

DELETE FROM users WHERE team_id = ?;

DELETE FROM contact_info WHERE student_id = ?;
