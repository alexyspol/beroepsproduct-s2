SELECT *
FROM users;

SELECT t.team_name, c.phone, c.email, c.residence, c.skill
FROM teams t, contact_info c
WHERE t.id = ? AND c.student_id = ?;

UPDATE users
SET first_name = ?, last_name = ?, dob = ?
WHERE student_id = ?;

UPDATE teams
SET team_name = ?
WHERE id = ?;

UPDATE contact_info
SET phone = ?, email = ?, residence = ?, skill = ?
WHERE student_id = ?;
