DROP TABLE `ticket_info`;
CREATE TABLE `ticket_info` (
    `id`	            int	primary key auto_increment,
    `festival_id`   	int,
    `day_type`	        varchar(10),
    `booking_day`	    datetime,
    `total_quantity`	int,
    `adult_price`   	int,
    `youth_price`	    int,
    `open_date`	        datetime,
    `deadline_date`	    datetime,
    `created_at`	    datetime default now()
);

DROP TABLE `ticket_earlybird_discount`;
CREATE TABLE `ticket_earlybird_discount` (
    `id`	                int	primary key auto_increment,
    `info_id`	            int,
    `discount`	            double,
    `discount_quantity`	    int,
    `remaining_quantity`	int,
    `created_at`	        datetime default now()
);

DROP TABLE `ticket_booking`;
CREATE TABLE `ticket_booking` (
    `id`	            int	primary key auto_increment,
    `user_id`	        int,
    `info_id`	        int,
    `adult_quantity`	int,
    `youth_quantity`	int,
    `total_price`       int,
    `is_use`			varchar(1) default 'N',
    `payment_status`	varchar(1) default 'N',
    `created_at`	    datetime default now()
);

DROP TABLE `ticket_review`;
CREATE TABLE `ticket_review` (
    `id`	        int	primary key auto_increment,
    `user_id`	    int,
    `booking_id`	int,
    `title`	        varchar(50),
    `content`	    text,
    `read_count`	int,
    `created_at`	datetime default now()
);

DROP TABLE `ticket_review_image`;
CREATE TABLE `ticket_review_image` (
    `id`	        int primary key auto_increment,
    `review_id`	    int,
    `url`	  		varchar(200),
    `created_at`	datetime default now()
);

DROP TABLE `ticket_review_like`;
CREATE TABLE `ticket_review_like` (
    `id`	        int primary key auto_increment,
    `user_id`	    int,
    `review_id`	    int,
    `created_at`	datetime default now()
);

DROP TABLE `ticket_payment`;
CREATE TABLE `ticket_payment` (
    `id`	        int primary key auto_increment,
    `booking_id`	int,
    `tid`			varchar(200),
    `cid`			varchar(200),
    `created_at`	datetime default now()
);
