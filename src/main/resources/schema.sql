CREATE TABLE IF NOT EXISTS users (
    student_id TEXT PRIMARY KEY,
    first_name TEXT,
    last_name TEXT,
    dob TEXT,
    team_id INTEGER REFERENCES teams(id)
);

CREATE TABLE IF NOT EXISTS teams (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    team_name TEXT
);

CREATE TABLE IF NOT EXISTS contact_info (
    student_id TEXT PRIMARY KEY REFERENCES users(student_id),
    phone TEXT,
    email TEXT,
    residence TEXT,
    skill TEXT
);
