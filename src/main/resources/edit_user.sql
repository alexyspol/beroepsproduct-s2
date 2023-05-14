-- select_all_users
SELECT *
FROM users;

-- select_related_information
SELECT t.team_name, c.phone, c.email, c.residence, c.skill
FROM users u, teams t, contact_info c
WHERE u.student_id = ? AND t.id = ? AND c.student_id = ?;

-- update_user
UPDATE users
SET first_name = ?, last_name = ?, dob = ?
WHERE student_id = ?;

-- change_team
UPDATE users
SET team_id = (
  SELECT id
  FROM teams
  WHERE team_name = ?
)
WHERE student_id = ?;

-- update_contact_info
UPDATE contact_info
SET phone = ?, email = ?, residence = ?, skill = ?
WHERE student_id = ?;
