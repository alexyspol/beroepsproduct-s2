SELECT t.team_name,
       u.first_name || ' ' || u.last_name AS full_name
FROM teams t, users u
WHERE u.team_id = t.id;
