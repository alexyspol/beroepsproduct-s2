-- select_everything
SELECT u.*, t.team_name, c.*
FROM users u
LEFT JOIN teams t ON u.team_id = t.id
LEFT JOIN contact_info c ON c.student_id = u.student_id;
