DROP TABLE rental_order;
CREATE TABLE rental_order(
	id int PRIMARY KEY AUTO_INCREMENT,
	user_id int,
	rental_item_id int,
	receiver varchar(30),
	count int,
	payment_method varchar(30),
	rental_order_status varchar(30),
	rental_days int,
	total_price int,
	created_at datetime DEFAULT NOW()
);

DROP TABLE rental_item;
CREATE TABLE rental_item(
	id int PRIMARY KEY AUTO_INCREMENT,
	category_id int,
	rental_title varchar(30),
	title varchar(50),
	content text,
	image varchar(500),
	stock_total int,
	rental_location varchar(500),
	price int,
	deposit int,
	created_at datetime DEFAULT NOW()
);

DROP TABLE rental_category;
CREATE TABLE rental_category(
	id int PRIMARY KEY AUTO_INCREMENT,
	name varchar(50)
);

DROP TABLE rental_detail_image;
CREATE TABLE rental_detail_image(
	id int PRIMARY KEY AUTO_INCREMENT,
	rental_item_id int,
	detail_image varchar(500)
);

DROP TABLE rental_like;
CREATE TABLE rental_like(
	id int PRIMARY KEY AUTO_INCREMENT,
	item_id int,
	user_id int,
	created_at datetime DEFAULT NOW()
);

DROP TABLE rental_review;
CREATE TABLE rental_review(
	id int PRIMARY KEY AUTO_INCREMENT,
	user_id int,
	order_id int,
	content text,
    image varchar(500),
    item_condition varchar(50),
	rating double,
	created_at datetime DEFAULT NOW()
);

DROP TABLE rental_review_like;
CREATE TABLE rental_review_like(
    id int PRIMARY KEY AUTO_INCREMENT,
    review_id int,
    user_id int,
    created_at datetime DEFAULT NOW()
);
