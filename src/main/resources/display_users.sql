-- select_everything
SELECT u.*, t.team_name, c.*
FROM users u
LEFT JOIN teams t ON u.team_id = t.id
LEFT JOIN contact_info c ON u.student_id = c.student_id;
