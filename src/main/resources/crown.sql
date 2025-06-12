CREATE DATABASE IF NOT EXISTS crown;

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    email VARCHAR(255),
    phone VARCHAR(255),
    google_calendar_id VARCHAR(255),
    google_access_token VARCHAR(255),
    role ENUM('DOCTOR', 'ADMIN', 'RECEPTIONIST'),
    password VARCHAR(255)
);

CREATE TABLE patient (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    email VARCHAR(255),
    phone VARCHAR(255),
    date_of_birth DATE
);

CREATE TABLE appointment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    start_date_time DATETIME,
    end_date_time DATETIME,
    status ENUM('SCHEDULED', 'CANCELLED', 'COMPLETED'),
    notes TEXT,
    google_event_id VARCHAR(255),
    patient_id BIGINT,
    doctor_id BIGINT,
    FOREIGN KEY (patient_id) REFERENCES patient(id),
    FOREIGN KEY (doctor_id) REFERENCES users(id)
);

CREATE TABLE doctor_availability (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    day_of_week ENUM('MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY'),
    start_hour TIME,
    end_hour TIME,
    active BOOLEAN,
    doctor_id BIGINT,
    FOREIGN KEY (doctor_id) REFERENCES users(id)
);

CREATE TABLE conversation (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    topic VARCHAR(255),
    is_group BOOLEAN,
    is_public BOOLEAN
);

CREATE TABLE conversation_participants (
    conversation_id BIGINT,
    user_id BIGINT,
    PRIMARY KEY (conversation_id, user_id),
    FOREIGN KEY (conversation_id) REFERENCES conversation(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE message (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sender_id BIGINT,
    subject VARCHAR(255),
    content TEXT,
    sent_at DATETIME,
    status ENUM('UNREAD', 'READ'),
    conversation_id BIGINT,
    FOREIGN KEY (sender_id) REFERENCES users(id),
    FOREIGN KEY (conversation_id) REFERENCES conversation(id)
);

CREATE TABLE message_recipients (
    message_id BIGINT,
    user_id BIGINT,
    PRIMARY KEY (message_id, user_id),
    FOREIGN KEY (message_id) REFERENCES message(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE medical_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255),
    description TEXT,
    created_at DATETIME,
    archived BOOLEAN,
    patient_id BIGINT,
    created_by_id BIGINT,
    FOREIGN KEY (patient_id) REFERENCES patient(id),
    FOREIGN KEY (created_by_id) REFERENCES users(id)
);

CREATE TABLE attachment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    filename VARCHAR(255),
    content_type VARCHAR(255),
    data LONGBLOB,
    attachment_type ENUM('MESSAGE', 'MEDICAL_RECORD', 'PATIENT'),
    message_id BIGINT,
    patient_id BIGINT,
    medical_record_id BIGINT,
    FOREIGN KEY (message_id) REFERENCES message(id),
    FOREIGN KEY (patient_id) REFERENCES patient(id),
    FOREIGN KEY (medical_record_id) REFERENCES medical_record(id)
);
