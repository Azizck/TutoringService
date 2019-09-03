dro DATABASE pro;
CREATE DATABASE pro;
use pro;



-- apointment table 
	CREATE TABLE APPT(
	appt_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	 subject varchar(255),
	 date date,
	 time int,
	duration INT (2),
	notes varchar(255),
	fee decimal (6,2)
	);


-- student apointment
CREATE TABLE STUD_APPT(
		id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
		student_id INT,
		appt_id  INT, 
	  FOREIGN KEY (student_id) REFERENCES SEC_USER(USER_ID),
	   FOREIGN KEY (appt_id) REFERENCES appt(appt_id)
	);


-- payment
CREATE TABLE payment (
		card_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
		type varchar(255),
		cardnumber varchar(255),
		expirydate varchar(255)
);

-- student payment 
CREATE TABLE student_payment(
		st_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
		 student_id int not null,
		 card_id int not null, 
	  FOREIGN KEY (student_id) REFERENCES SEC_USER(USER_ID),
	   FOREIGN KEY (card_id) REFERENCES payment(card_id)
); 
-- security user 
create table SEC_USER
(
  USER_ID           INT NOT NULL Primary Key AUTO_INCREMENT,
  USER_NAME         VARCHAR(36) NOT NULL UNIQUE,
  ENCRYPTED_PASSWORD VARCHAR(128) NOT NULL,
  ENABLED           BIT NOT NULL 
) ;

create table SEC_ROLE
(
  ROLE_ID   BIGINT NOT NULL Primary Key AUTO_INCREMENT,
  ROLE_NAME VARCHAR(30) NOT NULL UNIQUE
) ;


create table USER_ROLE
(
  ID      BIGINT NOT NULL Primary Key AUTO_INCREMENT,
  USER_ID BIGINT NOT NULL,
  ROLE_ID BIGINT NOT NULL
);



alter table USER_ROLE
  add constraint USER_ROLE_FK1 foreign key (USER_ID)
  references SEC_USER (USER_ID);
 
alter table USER_ROLE
  add constraint USER_ROLE_FK2 foreign key (ROLE_ID)
  references SEC_ROLE (ROLE_ID);

 
insert into sec_role (ROLE_NAME)
values ('ROLE_ADMIN');
insert into sec_role (ROLE_NAME)
values ('ROLE_STUDENT');
-- table that has info about tutor
create table tutor (min date, 
	max date, 
	subject1 VARCHAR(255), subject2 VARCHAR(255), subject3 VARCHAR(255), subject4 VARCHAR(255), subject5 VARCHAR(255), subject6 VARCHAR(255), pricePerHour float(2.2));

COMMIT;

