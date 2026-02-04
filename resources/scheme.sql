-- University President Election Management System Database Schema

DROP TABLE IF EXISTS candidates CASCADE;
DROP TABLE IF EXISTS students CASCADE;
DROP TABLE IF EXISTS elections CASCADE;

CREATE TABLE elections (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    academic_year VARCHAR(50) NOT NULL
);

CREATE TABLE candidates (
     id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    major VARCHAR(255) NOT NULL,
    year_of_study INTEGER NOT NULL CHECK(year_of_study >= 2 AND year_of_study <= 4),
    campaign TEXT,
    election_id INTEGER NOT NULL,
    vote_count INT default 0;
    FOREIGN KEY (election_id) REFERENCES elections(id) ON DELETE CASCADE
);

CREATE TABLE students (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    student_id VARCHAR(50) UNIQUE NOT NULL,
    major VARCHAR(255) NOT NULL,
    year_of_study INTEGER NOT NULL CHECK(year_of_study >= 1 AND year_of_study <= 4),
    has_voted BOOLEAN DEFAULT FALSE
);

INSERT INTO elections (name, start_date, end_date, academic_year) VALUES
    ('University President Election 2026', '2026-01-10', '2026-01-19', '2026-2027'),

INSERT INTO candidates (name, major, year_of_study, campaign, election_id) VALUES
    ('Zhubanazarova Ainaz', 'Computer Science', 3, 'Innovation and Student Welfare', 1),
    ('Bekbolat Aruzhan', 'Software Engineering', 2, 'New learning platforms', 1),
    ('Daurenuly Alisher', 'Cybersecurity', 3, 'Comfort in Campus', 1);

INSERT INTO students (name, student_id, major, year_of_study, has_voted) VALUES
    ('Arguan Bakikair', 'S001', 'Software Engineering', 1, TRUE),
    ('Dastan Nursultanov ', 'S002', 'CS', 3, FALSE),
    ('Ershat Diasov', 'S003', 'IT', 1, FALSE),
    ('Altynai Assylhanova', 'S004', 'Data Science', 2, TRUE);