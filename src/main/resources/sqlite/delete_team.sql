-- First, ask the user which team to delete
SELECT * FROM teams;

-- Then, find the users associated with the selected team
SELECT student_id, first_name, last_name FROM users WHERE team_id = ?;

-- Lastly, delete the team along with the users and their contact information 
DELETE FROM teams WHERE id = ?;
DELETE FROM users WHERE team_id = ?;
DELETE FROM contact_info WHERE student_id = ?;
