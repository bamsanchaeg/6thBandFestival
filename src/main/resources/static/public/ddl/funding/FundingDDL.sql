# 프로젝트 카테고리 : 아직 미생성
DROP TABLE `funding_project_category`;
CREATE TABLE `funding_project_category`(
	`id` int PRIMARY KEY AUTO_INCREMENT,
	`name` varchar(50)
);
-- SELECT * FROM `funding_project_category`;
INSERT INTO funding_project_category(name) values('공연');
INSERT INTO funding_project_category(name) values('페스티발');
INSERT INTO funding_project_category(name) values('앨범');
INSERT INTO funding_project_category(name) values('쇼케이스');
-- INSERT INTO funding_project_category(name) values('가수');

# 프로젝트 게시판
DROP TABLE `funding_project`;
CREATE TABLE `funding_project`(
	`id` int PRIMARY KEY AUTO_INCREMENT,
	`user_id` int,
	`category_id` int,
	`project_title` varchar(30),
	`project_desc` varchar(50),
	`thumbnail_location` varchar(500),
	`youtube_url` varchar(100),
	`expect_amount` int,
	`start_at` datetime,
	`end_at` datetime,
	`project_introduce` text,
	`budget_desc` text,
	`schedule_desc` text,
	`team_desc` text,
	`reward_desc` text,
	`status` varchar(10) DEFAULT '작성중',
	`read_count` int DEFAULT 0,
	`created_at` datetime DEFAULT NOW()
);
-- SELECT * FROM funding_project;

# 프로젝트 창작자 정보
DROP TABLE `funding_project_creator`;
CREATE TABLE `funding_project_creator` (
	`id` int PRIMARY KEY AUTO_INCREMENT,
	`user_id` int,
	`creator_name` varchar(20),
	`profile_location` varchar(500),
	`introduce` varchar(300),
	`bank_name` varchar(20),
	`account_number` varchar(20),
	`account_holder` varchar(20),
	`account_birth` varchar(10),
	`created_at` datetime DEFAULT NOW()
);
-- SELECT * FROM `funding_project_creator`;

# 프로젝트 소개 첨부파일
DROP TABLE `funding_introduce_file`;
CREATE TABLE `funding_introduce_file` (
	`id` int PRIMARY KEY AUTO_INCREMENT,
	`project_id` int,
	`location` varchar(200),
	`created_at` datetime DEFAULT NOW()
);
-- SELECT * FROM `funding_introduce_file`;

# 프로젝트 팀소개 첨부파일
DROP TABLE `funding_teamdesc_file`;
CREATE TABLE `funding_teamdesc_file` (
	`id` int PRIMARY KEY AUTO_INCREMENT,
	`project_id` int,
	`location` varchar(200),
	`created_at` datetime DEFAULT NOW()
);
-- SELECT * FROM `funding_teamdesc_file`;

# 프로젝트 선물설명 첨부파일
DROP TABLE `funding_rewarddesc_file`;
CREATE TABLE `funding_rewarddesc_file` (
	`id` int PRIMARY KEY AUTO_INCREMENT,
	`project_id` int,
	`location` varchar(200),
	`created_at` datetime DEFAULT NOW()
);
-- SELECT * FROM `funding_rewarddesc_file`;

# 프로젝트 아이템
DROP TABLE `funding_project_item`;
CREATE TABLE `funding_project_item`(
	`id` int PRIMARY KEY AUTO_INCREMENT,
	`project_id` int,
	`name` varchar(100),
	`option_type` varchar(2),
	`created_at` datetime DEFAULT NOW()
);
-- SELECT * FROM `funding_project_item`;

# 프로젝트 아이템 옵션
DROP TABLE `funding_project_item_option`;
CREATE TABLE `funding_project_item_option`(
	`id` int PRIMARY KEY AUTO_INCREMENT,
	`item_id` int,
	`option_name` varchar(100),
	`created_at` datetime DEFAULT NOW()
);
-- SELECT * FROM `funding_project_item_option`;

# 프로젝트 선물
DROP TABLE `funding_project_reward`;
CREATE TABLE `funding_project_reward`(
	`id` int PRIMARY KEY AUTO_INCREMENT,
	`project_id` int,
	`title` varchar(50),
	`amount` int,
	`count_limit` int,
	`expect_date` int,
	`is_delivery` varchar(1),
	`created_at` datetime DEFAULT NOW()
);
-- SELECT * FROM `funding_project_reward`;

# 프로젝트 선물 아이템 매핑
DROP TABLE `funding_project_reward_item`;
CREATE TABLE `funding_project_reward_item`(
	`id` int PRIMARY KEY AUTO_INCREMENT,
	`reward_id` int,
	`item_id` int,
	`created_at` datetime DEFAULT NOW()
);
-- SELECT * FROM `funding_project_reward_item`;

# 프로젝트 심사 이력
DROP TABLE `funding_approval_history`;
CREATE TABLE `funding_approval_history`(
	`id` int PRIMARY KEY AUTO_INCREMENT,
	`project_id` int,
	`result` varchar(10),
	`content` varchar(500),
	`created_at` datetime DEFAULT NOW()
);
-- SELECT * FROM funding_approval_history;

# 프로젝트 후원 :: 추가한 항목 > delivery_at , payment_type
DROP TABLE `funding_support`;
CREATE TABLE `funding_support` (
	`id` int PRIMARY KEY AUTO_INCREMENT,
	`user_id` int,
	`payment_type` varchar(10),
	`status` varchar(10),
	`pay_status` varchar(10),
	`delivery_status` varchar(10),
	`support_at` datetime,
	`cancel_at` datetime,
	`delivery_at` datetime,
	`created_at` datetime DEFAULT NOW()
);
-- SELECT * FROM `funding_support`;

# 후원 배송지
DROP TABLE `funding_support_delivery`;
CREATE TABLE `funding_support_delivery` (
	`id` int PRIMARY KEY AUTO_INCREMENT,
	`support_id` int,
-- 	`status` varchar(10),
	`zip_code` varchar(10),
	`address` varchar(300),
	`address_detail` varchar(30),
	`recipient_name` varchar(20),
	`recipient_phone` varchar(40),
	`recipient_at` datetime
);
-- SELECT * FROM `funding_support_delivery`;

# 후원 프로젝트 선물
DROP TABLE `funding_support_reward`;
CREATE TABLE `funding_support_reward` (
	`id` int PRIMARY KEY AUTO_INCREMENT,
	`support_id` int,
	`reward_id` int,
	`quantity` int,
	`created_at` datetime DEFAULT NOW()
);
-- SELECT * FROM `funding_support_reward`;

# 후원 프로젝트 선물 아이템 옵션
DROP TABLE `funding_support_item_option`;
CREATE TABLE `funding_support_item_option` (
	`id` int PRIMARY KEY AUTO_INCREMENT,
	`support_reward_id` int,
	`option_id` int,
	`option_content` varchar(100)
);
-- SELECT * FROM `funding_support_item_option`;

# 프로젝트 후기 키워드 목록
DROP TABLE `funding_project_review_keyword`;
CREATE TABLE `funding_project_review_keyword` (
	`id` int PRIMARY KEY AUTO_INCREMENT,
	`keyword` varchar(20)
);
-- SELECT * FROM `funding_project_review_keyword`;
INSERT INTO funding_project_review_keyword(keyword) VALUES('창의적이에요');
INSERT INTO funding_project_review_keyword(keyword) VALUES('즐거움을 줘요');
INSERT INTO funding_project_review_keyword(keyword) VALUES('특별해요');
INSERT INTO funding_project_review_keyword(keyword) VALUES('소통이 잘 돼요');
INSERT INTO funding_project_review_keyword(keyword) VALUES('의미 있어요');
INSERT INTO funding_project_review_keyword(keyword) VALUES('퀄리티가 좋아요');
INSERT INTO funding_project_review_keyword(keyword) VALUES('약속을 잘 지켜요');
INSERT INTO funding_project_review_keyword(keyword) VALUES('유용해요');
INSERT INTO funding_project_review_keyword(keyword) VALUES('해당 없음');

# 프로젝트 후기
DROP TABLE `funding_project_review`;
CREATE TABLE `funding_project_review` (
	`id` int PRIMARY KEY AUTO_INCREMENT,
	`support_id` int,
	`content` varchar(1000),
	`created_at` datetime DEFAULT NOW()
);

# 프로젝트 후기 선택 키워드
DROP TABLE `funding_project_review_selected_keyword`;
CREATE TABLE `funding_project_review_selected_keyword` (
	`id` int PRIMARY KEY AUTO_INCREMENT,
	`review_id` int,
	`keyword_id` int
);
-- SELECT * FROM `funding_project_review_selected_keyword`;

# 프로젝트 후기 첨부파일
DROP TABLE `funding_project_review_file`;
CREATE TABLE `funding_project_review_file` (
	`id` int PRIMARY KEY AUTO_INCREMENT,
	`review_id` int,
	`location` varchar(200),
	`created_at` datetime DEFAULT NOW()
);
-- SELECT * FROM `funding_project_review_file`;


# 프로젝트 커뮤니티 게시판
DROP TABLE `funding_project_community`;
CREATE TABLE `funding_project_community` (
	`id` int PRIMARY KEY AUTO_INCREMENT,
	`project_id` int,
	`user_id` int,
	`content` varchar(1000),
	`created_at` datetime DEFAULT NOW()
);
--  SELECT * FROM `funding_project_community`;

# 프로젝트 커뮤니티 게시판 댓글
DROP TABLE `funding_project_community_comment`;
CREATE TABLE `funding_project_community_comment` (
	`id` int PRIMARY KEY AUTO_INCREMENT,
	`community_id` int,
	`user_id` int,
	`content` varchar(1000),
	`created_at` datetime DEFAULT NOW()
);
-- SELECT * FROM `funding_project_community_comment`;

# 프로젝트 업데이트 게시판
DROP TABLE `funding_project_update`;
CREATE TABLE `funding_project_update` (
	`id` int PRIMARY KEY AUTO_INCREMENT,
	`project_id` int,
	`user_id` int,
	`content` varchar(5000),
	`created_at` datetime DEFAULT NOW()
);
-- SELECT * FROM `funding_project_update`;

# 프로젝트 업데이트 게시판 댓글
DROP TABLE `funding_project_update_comment`;
CREATE TABLE `funding_project_update_comment` (
	`id` int PRIMARY KEY AUTO_INCREMENT,
	`update_id` int,
	`user_id` int,
	`content` varchar(1000),
	`created_at` datetime DEFAULT NOW()
);
-- SELECT * FROM `funding_project_update_comment`;

## 관심 프로젝트
DROP TABLE `funding_project_like`;
CREATE TABLE `funding_project_like` (
	`id` int PRIMARY KEY AUTO_INCREMENT,
	`project_id` int,
	`user_id` int,
	`created_at` datetime DEFAULT NOW()
);
-- SELECT * FROM `funding_project_like`;

## 프로젝트 스케줄러 로그 테이블
DROP TABLE `funding_schedule_log`;
CREATE TABLE `funding_schedule_log`(
	`id` int PRIMARY KEY AUTO_INCREMENT,
	`project_id` int,
	`old_status` varchar(10),
	`new_status` varchar(10),
	`created_at` datetime DEFAULT NOW()
);
-- SELECT * FROM `funding_schedule_log`;

## 정산 내역 테이블
DROP TABLE `funding_settlement_details`;
CREATE TABLE `funding_settlement_details` (
	`id` int PRIMARY KEY AUTO_INCREMENT,
	`project_id` int,
	`settlement_amount` int,
	`settlement_fee` int,
	`settlement_status` varchar(10),
	`settlement_date` datetime,
	`created_at` datetime DEFAULT NOW()
);
-- SELECT * FROM `funding_settlement_details`;

## 카카오페이 결제 table
DROP TABLE `funding_payment`;
CREATE TABLE `funding_payment` (
	`id` int PRIMARY KEY AUTO_INCREMENT,
	`support_id` int,
	`tid` varchar(200),
	`cid` varchar(200),
	`created_at` datetime DEFAULT NOW()
);
-- SELECT * FROM `funding_payment`;

## ChatGPT 리뷰 요약
DROP TABLE `funding_project_review_chatGPT`;
CREATE TABLE `funding_project_review_chatGPT` (
	`id` int PRIMARY KEY AUTO_INCREMENT,
	`review_id` int,
	`gpt_content` varchar(100),
	`created_at` datetime DEFAULT NOW()
);
-- SELECT * FROM `funding_project_review_chatGPT`;