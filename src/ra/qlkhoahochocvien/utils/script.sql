-- 1. Create database QLHocVien
create database QLHocVien;
create schema qlhocvien;
set search_path to qlhocvien;

-- 2. Create table admin
create table Admin(
    id serial primary key,
    username varchar(50) unique not null,
    password varchar(255) not null
);

-- 3. Create table Student
create table Student(
    id serial primary key,
    name varchar(100) not null,
    dob date not null,
    email varchar(100) not null,
    sex bit not null,
    phone varchar(20),
    password varchar(255) not null,
    create_at date default current_date
);

-- 4. Create table Course
create table Course(
    id serial primary key,
    name varchar(100) not null,
    duration int not null,
    instructor varchar(100) not null,
    create_at date default current_date
);

-- 5a. Create enum for status
create type status_values as enum('WAITING', 'DENIED', 'CANCEL', 'CONFIRM', 'DELETED');

-- 5. Create table Enrollment
create table Enrollment(
    id serial primary key,
    student_id int references Student(id) not null,
    course_id int references Course(id) not null,
    registered_at timestamp default current_timestamp,
    status status_values
);

-- 6. Create fake data for Courses
insert into course (name, duration, instructor)
values ('Cơ sở lập trình', 90, 'Nguyễn Văn A'),
       ('Đại số tuyến tính', 50, 'Trần Văn B'),
       ('Giải tích 2', 70, 'Lê Vũ Phong');

-- 7. Create fake data for Students
insert into student (name, dob, email, sex, phone, password)
values ('Nguyễn Văn C', '2000-01-15', 'nvc@gmail.com', 1::bit,'0123456778', 'password123'),
       ('Trần Thị D', '1999-05-20', 'tranthid@gmail.com', 0::bit,'012356789', 'password456'),
       ('Lê Văn E', '2001-09-10', 'levane@gmail.com', 1::bit, '0312980743', 'password789');

-- 8. Create fake data for Enrollments
insert into enrollment (student_id, course_id, status)
values (1, 1, 'CONFIRM'),
         (1, 2, 'WAITING'),
            (2, 1, 'DENIED'),
            (2, 3, 'CONFIRM'),
            (3, 2, 'CANCEL');
