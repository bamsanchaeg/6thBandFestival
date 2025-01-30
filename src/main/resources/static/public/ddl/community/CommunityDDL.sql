DROP table six_user;
CREATE table six_user(
	id INT primary key auto_increment,
	account_name varchar(20),  
	password varchar(200),
	name varchar(20),
	nickname varchar(20),
	email varchar(400),
	gender varchar(3),
	birth datetime,
	phone varchar(40),
	profile_img varchar(1000),
	created_at datetime default now()

)

DROP TABLE club;
CREATE TABLE club (
	id INT primary key auto_increment,
    user_id INT,
    club_name VARCHAR(50),
    club_explanation VARCHAR(1000),
    max_members INT,
    club_activation INT default 0,
    club_logo VARCHAR(100),
    question1 varchar(1000),
    question2 varchar(1000),
    club_location varchar(1000),
    club_click_count int,
    created_at datetime default now()

);
-- 모임별 질문문항 만들기
-- ALTER TABLE club add COLUMN question1 varchar(1000);
-- ALTER TABLE club add COLUMN question2 varchar(1000);
-- ALTER TABLE club add COLUMN club_location varchar(1000);
-- ALTER TABLE club add COLUMN club_click_count int;



-- ALTER TABLE club ADD COLUMN authority VARCHAR(255) DEFAULT '방장';

DROP TABLE club_member;
CREATE TABLE club_member (
	id INT primary key auto_increment,
    club_member_id INT,
    club_chairman_right BOOLEAN,
    created_at datetime default now()
 
);

DROP TABLE club_member_regist;
CREATE TABLE club_member_regist (
	id INT primary key auto_increment,
    user_id INT,
    club_id INT,
    regist_reason VARCHAR(1000),
    self_introduction VARCHAR(1000),
    member_activation INT default 0,
    club_member_active VARCHAR(10) default 'N',
    created_at datetime default now()
);

-- ALTER TABLE club_member_regist ADD COLUMN club_member_active VARCHAR(10);
-- alter table club_member_regist drop COLUMN club_member_active;

DROP TABLE club_board;
CREATE TABLE club_board (
	id INT primary key auto_increment,
    club_member_id INT,
    club_category_id INT,
    title VARCHAR(500),
    content VARCHAR(4000),
    club_id INT,
    read_count int,
    created_at datetime default now()

);

-- ALTER TABLE club_board add COLUMN read_count int;


drop table club_board_category ;
create table club_board_category (
	id INT primary key auto_increment,
	board_category_id int,
	board_id int
	
);

DROP TABLE board_category;
CREATE TABLE board_category (
	id INT primary key auto_increment,
    name VARCHAR(50)
);

INSERT  into board_category(name) values('가입 인사');
INSERT  into board_category(name) values('잡담');
INSERT  into board_category(name) values('궁금해요');
INSERT  into board_category(name) values('기타');


DROP TABLE club_board_detail_img;
CREATE TABLE club_board_detail_img (
	id INT primary key auto_increment,
	club_id int,
    club_board_id INT,
    location VARCHAR(1000)
);

DROP TABLE club_board_comment;
CREATE TABLE club_board_comment (
	id INT primary key auto_increment,
    club_board_id INT,
    club_member_id INT,
    content VARCHAR(4000),
    club_id INT,
    created_at datetime default now()   
);

DROP TABLE club_board_like;
CREATE TABLE club_board_like (
	id INT primary key auto_increment,
    club_member_id INT,
    club_board_id INT,
    club_id INT,
    created_at datetime default now()

);

DROP table club_photo;
CREATE TABLE club_photo(
	id INT primary key auto_increment,
	club_member_id	int,
	club_photo_img	varchar(1000),
	club_id	int,
	created_at datetime default now()

);


DROP TABLE club_photo_comment;
CREATE TABLE club_photo_comment (
	id INT primary key auto_increment,
    club_photo_id INT,
    club_member_id INT,
    content VARCHAR(1000),
    club_id INT,
    created_at datetime default now()
);

DROP TABLE club_photo_like;
CREATE TABLE club_photo_like (
    id INT primary key auto_increment,
    club_photo_id INT,
    club_member_id INT,
    created_at datetime default now(),
);

DROP TABLE my_select_vote;
CREATE TABLE my_select_vote (
	id INT primary key auto_increment,
    option_id INT,
    vote_id INT,
    club_member_id INT,
    club_id INT,
    created_at datetime default now()
);


DROP TABLE club_vote;
CREATE TABLE club_vote (
	id INT primary key auto_increment,
    question_title VARCHAR(500),
    club_member_id INT,
    club_id INT,
    final_date DATE,
    created_at datetime default now()
);

DROP TABLE club_vote_option;
CREATE TABLE club_vote_option (
	id INT primary key auto_increment,
	vote_id INT,
    question_content VARCHAR(1000),
    question_image VARCHAR(1000),
    club_id INT,
    club_member_id INT,
    created_at datetime default now()
);

DROP TABLE club_schedule;
CREATE TABLE club_schedule (
	id INT primary key auto_increment,
	schedule_title varchar(300),
	schedule_explanation varchar(1000),
	start_date datetime,
	end_date datetime,
	schedule_location varchar(50),
	max_members int,
	schedule_image varchar(2000),
	club_id int,
	club_member_id int,
	weedo double,
	gyungdo double,
    created_at datetime default now()

);


drop table club_schedule_member;
CREATE TABLE club_schedule_member (
	schedule_id int,
	club_member_id int,
	club_id int

);

drop table club_board_report;
create table club_board_report(
	id INT primary key auto_increment,
	club_id int,
	club_board_id int,
	club_member_id int,
	report_reason varchar(1000),
    created_at datetime default now()
	

)

drop table chatGpt_Review;
CREATE table chatGpt_Review(
	id INT primary key auto_increment,
	club_board_id int,
	Gpt_Review varchar(500),
	comment_id int,
    created_at datetime default now()
)
