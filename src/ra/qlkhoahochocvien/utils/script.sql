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
    create_at date
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
create type status_values as enum('WAITING', 'DENIED', 'CANCEL', 'CONFIRM');

-- 5. Create table Enrollment
create table Enrollment(
    id serial primary key,
    student_id int references Student(id) not null,
    course_id int references Course(id) not null,
    registered_at timestamp default current_timestamp,
    status status_values
);


