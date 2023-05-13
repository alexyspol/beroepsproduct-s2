-- select_all_teams_users
SELECT t.team_name, u.first_name || ' ' || u.last_name AS full_name
FROM teams t
LEFT OUTER JOIN users u ON t.id = u.team_id
ORDER BY t.team_name;
