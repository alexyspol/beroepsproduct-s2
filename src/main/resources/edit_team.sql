-- select_all_teams
SELECT *
FROM teams;

-- change_team_name
UPDATE teams
SET team_name = ?
WHERE id = ?;
