-- users table
INSERT INTO users (student_id, first_name, last_name, dob, team_id) VALUES
('001', 'John', 'Doe', '1995-07-01', 1),
('002', 'Jane', 'Smith', '1996-10-05', 1),
('003', 'Bob', 'Johnson', '1997-01-15', 2),
('004', 'Sarah', 'Lee', '1998-04-30', 2),
('005', 'David', 'Kim', '1995-09-12', 3),
('006', 'Emily', 'Garcia', '1996-12-25', 3),
('007', 'Jason', 'Nguyen', '1997-05-18', 4),
('008', 'Megan', 'Wong', '1998-08-22', 4);

-- teams table
INSERT INTO teams (team_name) VALUES
('Team A'),
('Team B'),
('Team C'),
('Team D');

-- contact_info table
INSERT INTO contact_info (student_id, phone, email, residence, skill) VALUES
('001', '555-1234', 'johndoe@example.com', '123 Main St, Anytown USA', 'Java'),
('002', '555-5678', 'janesmith@example.com', '456 Elm St, Anytown USA', 'Python'),
('003', '555-9876', 'bobjohnson@example.com', '789 Oak St, Anytown USA', 'JavaScript'),
('004', '555-4321', 'sarahlee@example.com', '321 Pine St, Anytown USA', 'C#'),
('005', '555-2468', 'davidkim@example.com', '654 Maple St, Anytown USA', 'PHP'),
('006', '555-3690', 'emilygarcia@example.com', '987 Cedar St, Anytown USA', 'Ruby'),
('007', '555-1357', 'jasonnguyen@example.com', '246 Birch St, Anytown USA', 'Swift'),
('008', '555-5793', 'meganwong@example.com', '135 Walnut St, Anytown USA', 'C++');
