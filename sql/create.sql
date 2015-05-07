/*
Su Feng A20338748
CS425 project CREATE.SQL
Create relations
*/

DROP SEQUENCE users_seq;
DROP SEQUENCE bank_accounts_seq;
DROP SEQUENCE credit_cards_seq;
DROP SEQUENCE restaurants_seq;
DROP SEQUENCE restaurants_Reviews_seq;
DROP SEQUENCE laptop_comments_seq;


DROP TABLE Laptop_comments;
DROP TABLE State;
DROP TABLE Subject;
DROP TABLE Restaurants_reviews;
DROP TABLE Restaurants;
DROP TABLE Isleader;
DROP TABLE Join_group;
DROP TABLE Groups;
DROP TABLE Credit_cards;
DROP TABLE Bank_accounts;
DROP TABLE Users;


CREATE TABLE Users (
  id NUMBER(10) PRIMARY KEY,
  username VARCHAR2(20) NOT NULL UNIQUE,
  password VARCHAR2(20) NOT NULL,
  email VARCHAR2(50) NOT NULL,
  credit NUMBER(10) NOT NULL
);

create sequence users_seq
minvalue 0
start with 0 
increment by 1 
nomaxvalue; 

CREATE TABLE Bank_accounts (
  id NUMBER(10) PRIMARY KEY,
  user_id NUMBER(10) REFERENCES Users(id),
  bank VARCHAR2(20) NOT NULL,
  routing_number NUMBER(10) NOT NULL,
  account_number NUMBER(10) NOT NULL,
  init_bal NUMBER(10) NOT NULL
);

create sequence bank_accounts_seq
minvalue 0
start with 0 
increment by 1 
nomaxvalue; 

CREATE TABLE Credit_cards (
  id NUMBER(10) PRIMARY KEY,
  user_id NUMBER(10) REFERENCES Users(id),
  company VARCHAR2(20) NOT NULL,
  card_number NUMBER(10) NOT NULL,
  bill_addr VARCHAR2(50) NOT NULL,
  exp_date NUMBER(10) NOT NULL
);

create sequence credit_cards_seq
minvalue 0
start with 0 
increment by 1 
nomaxvalue; 

CREATE TABLE Groups (
  id NUMBER(10) PRIMARY KEY,
  name VARCHAR2(20) NOT NULL,
  description VARCHAR2(50)
);

CREATE TABLE Join_group (
  user_id NUMBER(10) REFERENCES Users(id),
  group_id NUMBER(10) REFERENCES Groups(id)
);

CREATE TABLE Isleader (
  user_id NUMBER(10) REFERENCES Users(id),
  group_id NUMBER(10) REFERENCES Groups(id)
);

CREATE TABLE Restaurants (
  id NUMBER(10) PRIMARY KEY,
  name VARCHAR2(20) NOT NULL,
  description VARCHAR2(50),
  rating NUMBER(10)
);

create sequence restaurants_seq
minvalue 0
start with 0 
increment by 1 
nomaxvalue; 

CREATE TABLE Restaurants_Reviews(
  id NUMBER(10) PRIMARY KEY,
  user_id NUMBER(10) REFERENCES Users(id),
  restaurant_id NUMBER(10) REFERENCES Restaurants(id),
  review_content VARCHAR2(100),
  good NUMBER(10) NOT NULL,
  bad NUMBER(10) NOT NULL,
  point NUMBER(10) NOT NULL
);

create sequence restaurants_Reviews_seq
minvalue 0
start with 0 
increment by 1 
nomaxvalue; 

CREATE TABLE Subject (
  id NUMBER(10) PRIMARY KEY,
  name VARCHAR2(20) NOT NULL
);

CREATE TABLE State (
  id NUMBER(10) PRIMARY KEY,
  name VARCHAR2(20) NOT NULL
);

CREATE TABLE Laptop_comments(
  id NUMBER(10) PRIMARY KEY,
  user_id NUMBER(10) REFERENCES Users(id),
  title VARCHAR2(50) NOT NULL,
  subject VARCHAR2(20) NOT NULL,
  brand VARCHAR2(20) NOT NULL,
  comment_content VARCHAR2(100),
  good NUMBER(10) NOT NULL,
  bad NUMBER(10) NOT NULL,
  price NUMBER(10) NOT NULL,
  state VARCHAR2(20) NOT NULL,
  point NUMBER(10) NOT NULL
);

create sequence laptop_comments_seq
minvalue 0
start with 0 
increment by 1 
nomaxvalue; 