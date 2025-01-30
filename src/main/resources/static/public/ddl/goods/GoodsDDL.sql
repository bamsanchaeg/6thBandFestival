## 굿즈 테이블
drop table goods;
create table goods(
	id int primary key auto_increment,
	category_id int,
	goods_title varchar(200),
	image varchar(10000),
	goods_count int,
	price int,
	detail text,
	created_at datetime default now()
);

##굿즈 할인
drop table goods_discount;
create table goods_discount(
	id int primary key auto_increment,
	goods_id int,
	discount double default '0',
	created_at datetime default now()
);

##굿즈 주문
drop table goods_order;
create table goods_order(
	id int primary key auto_increment,
	user_id int,
	goods_id int,
	goods_order_count int,
	order_price int,
	receiver varchar(30),
	address varchar(200),
	phone varchar(20),
	payment_info varchar(20) default '신용카드',
	request_details varchar(100),
	order_status varchar(20) default '결제완료',
	created_at datetime default now()
);

## 굿즈 위시리스트
drop table goods_wish;
create table goods_wish(
	id int primary key auto_increment,
	goods_id int,
	user_id int,
	created_at datetime default now()
);

## 굿즈 장바구니
drop table goods_shopperbag;
create table goods_shopperbag(
	id int primary key auto_increment,
	user_id int,
	goods_id int,
	item_count int
);

## 굿즈 후기 좋아요
drop table goods_review_like;
create table goods_review_like(
	id int primary key auto_increment,
	user_id int, 
	review_id int,
	created_at datetime default now()
);

## 굿즈 후기
drop table goods_review;
create table goods_review(
	id int primary key auto_increment,
	order_id int,
	content varchar(500),
	image varchar(2000),
	rating double,
	created_at datetime default now()
);

## 굿즈 후기 GPT요약
drop table goodsGPT_review;
create table goodsGPT_review(
	id int primary key auto_increment,
	comment_id int,
	Gpt_Review text,
	created_at datetime DEFAULT NOW()
);

## 굿즈 후기 판매자 답글
drop table goods_review_recommend;
create table goods_review_recommend(
	id int primary key auto_increment,
	goods_review_id int,
	recommend varchar(200),
	created_at datetime default now()
);

## 굿즈 카테고리
drop table goods_category;
create table goods_category(
	id int primary key auto_increment,
	name varchar(20)
);

insert into goods_category(	name ) values (	'MUSIC');
insert into goods_category(	name ) values (	'PHOTO');
insert into goods_category(	name ) values (	'CONCERT');
insert into goods_category(	name ) values (	'LIVING');
insert into goods_category(	name ) values (	'BEAUTY');
insert into goods_category(	name ) values (	'FASHION');
insert into goods_category(	name ) values (	'CUSTOMIZING');

## 굿즈 상세이미지
drop table goods_detailImage;
create table goods_detailImage(
	id int primary key auto_increment,
	goods_id int,
	multiple_image varchar(10000)
);

## 굿즈 후기 상세이미지
drop table goods_review_detailImage;
create table goods_review_detailImage(
	id int primary key auto_increment,
	review_id int,
	multiple_image varchar(2000)
);