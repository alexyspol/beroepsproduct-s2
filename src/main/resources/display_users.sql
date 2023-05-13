-- select_everything
SELECT u.student_id, u.first_name, u.last_name, u.dob,
       c.phone, c.email, c.residence, c.skill,
       t.team_name
FROM users u, contact_info c, teams t
WHERE c.student_id = u.student_id AND t.id = u.team_id;
