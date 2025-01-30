-- 고객센터팀
DROP TABLE `service_team`;
CREATE TABLE `service_team` (
    `id`	        int primary key auto_increment,
    `email`			varchar(100),
    `password`		varchar(100),
    `name`			varchar(20),
    `birth`			date,
    `gender`		varchar(10),
    `phone`			varchar(100),
    `profile`		varchar(200),
    `position`		varchar(20),
    `created_at`	datetime default now()
);

DROP TABLE `service_attendance_management`;
CREATE TABLE `service_attendance_management` (
    `id`	        int primary key auto_increment,
    `service_id`	int,
    `status`		varchar(20),
    `created_at`	datetime default now()
);

DROP TABLE `service_break_management`;
CREATE TABLE `service_break_management` (
    `id`	        int primary key auto_increment,
    `service_id`	int,
    `end_time`		datetime,
    `is_done`		varchar(1) default 'N',
    `created_at`	datetime default now()
);

DROP TABLE `service_team_schedule`;
CREATE TABLE `service_team_schedule` (
    `id`	        	int primary key auto_increment,
    `service_id`		int,
    `select_date`		date,
    `end_date`			date,
    `working_time`		time,
    `quitting_time`		time,
    `created_at`		datetime default now()
);

DROP TABLE `service_team_notice`;
CREATE TABLE `service_team_notice` (
    `id`	        int primary key auto_increment,
    `service_id`	int,
    `title`			varchar(50),
    `content`		text,
    `read_count`	int,
    `is_important`	varchar(1),
    `created_at`	datetime default now()
);

DROP TABLE `service_team_notice_image`;
CREATE TABLE `service_team_notice_image` (
    `id`	        int primary key auto_increment,
    `notice_id`	    int,
    `url`	  		varchar(200),
    `created_at`	datetime default now()
);

DROP TABLE `service_qna_category`;
CREATE TABLE `service_qna_category` (
    `id`	        	int primary key auto_increment,
    `category_title`	varchar(20)
);

insert into service_qna_category(category_title) values ('티켓 관련');
insert into service_qna_category(category_title) values ('대여 관련');
insert into service_qna_category(category_title) values ('상품 관련');
insert into service_qna_category(category_title) values ('티펀딩 관련');

DROP TABLE `service_qna_board`;
CREATE TABLE `service_qna_board` (
    `id`	        int primary key auto_increment,
    `category_id`	int,
    `user_id`		int,
    `service_id`	int,
    `title`			varchar(50),
    `content`		text,
    `status`		varchar(20),
    `created_at`	datetime default now()
);

DROP TABLE `service_qna_image`;
CREATE TABLE `service_qna_image` (
    `id`	        int primary key auto_increment,
    `board_id`	    int,
    `url`	  		varchar(200),
    `created_at`	datetime default now()
);

DROP TABLE `service_qna_answer`;
CREATE TABLE `service_qna_answer` (
    `id`	        int primary key auto_increment,
    `board_id`		int,
    `content`		text,
    `created_at`	datetime default now()
);

DROP TABLE `service_qna_answer_image`;
CREATE TABLE `service_qna_answer_image` (
    `id`	        int primary key auto_increment,
    `answer_id`	    int,
    `url`	  		varchar(200),
    `created_at`	datetime default now()
);

-- 채팅..
DROP TABLE `service_live_chat_room`;
CREATE TABLE `service_live_chat_room` (
    `id`	        int primary key auto_increment,
    `user_id`	    int,
    `service_id`    int,
    `is_active`	  	varchar(1) default 'Y',
    `created_at`	datetime default now()
);

DROP TABLE `service_live_chat`;
CREATE TABLE `service_live_chat` (
    `id`	        int primary key auto_increment,
    `room_id`	    int,
    `from_email`	varchar(100),
    `message`		text,
    `is_reading`	varchar(1) default 'N',
    `created_at`	datetime default now()
);

