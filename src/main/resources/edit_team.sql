SELECT *
FROM teams;

UPDATE teams
SET team_name = ?
WHERE id = ?;
