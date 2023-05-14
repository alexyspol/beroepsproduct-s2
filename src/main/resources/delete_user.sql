-- select_all_users
SELECT student_id, first_name, last_name
FROM users;

-- delete_single_user
DELETE FROM users
WHERE student_id = ?;

-- delete_contact_info
DELETE FROM contact_info
WHERE student_id = ?;
