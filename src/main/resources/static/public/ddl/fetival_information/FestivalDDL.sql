Select *
from festival_information;
DROP TABLE IF EXISTS `festival_information`;
CREATE TABLE `festival_information`
(
    `id`                         int PRIMARY KEY AUTO_INCREMENT,
    `festival_name`              varchar(100),
    `festival_location`          varchar(100),
    `festival_content`           text,
    `festival_reservationMethod` text,
    `festival_caution`           text,
    `thumbnail`                  varchar(200),
    `age_limit`                  varchar(100),
    `starting_date`              datetime,
    `end_date`                   datetime,
    `created_at`                 timestamp DEFAULT NOW()
);



Select *
from festival_information_artist;
DROP TABLE IF EXISTS `festival_information_artist`;
CREATE TABLE `festival_information_artist`
(
    `id`             int PRIMARY KEY AUTO_INCREMENT,
    `artist_name`    varchar(100),
    `artist_summary` varchar(500),
    `artist_detail`  text,
    `thumbnail`      varchar(200),
    `youtube_url`    varchar(400),
    `sns_url`        varchar(400),
    `created_at`     timestamp DEFAULT NOW()
);

# ALTER TABLE festival_information_artist ADD COLUMN artist_summary varchar(500);


Select *
from festival_information_artist_image;
DROP TABLE IF EXISTS `festival_information_artist_image`;
CREATE TABLE `festival_information_artist_image`
(
    `id`         int PRIMARY KEY AUTO_INCREMENT,
    `artist_id`  int,
    `images_url` varchar(200),
    `created_at` timestamp DEFAULT NOW()
);


Select *
from festival_information_performanceDate;
DROP TABLE IF EXISTS `festival_information_performanceDate`;
CREATE TABLE `festival_information_performanceDate`
(
    `id`               int PRIMARY KEY AUTO_INCREMENT,
    `festival_id`      int,
    `performance_date` date,
    `created_at`       timestamp DEFAULT NOW()
);

## 일자별 공연 새부 아티스트 분류
## 시스템에 의해 미리 인서트 되어야 하는 값
SELECT *
FROM festival_information_lineup;
DROP TABLE if exists `festival_information_lineup`;
CREATE TABLE `festival_information_lineup`
(
    `id`             INT PRIMARY KEY AUTO_INCREMENT,
    `performance_id` int,
    `artist_id`      int
);


Select *
from festival_information_image;
DROP TABLE IF EXISTS `festival_information_image`;
CREATE TABLE `festival_information_image`
(
    `id`          int PRIMARY KEY AUTO_INCREMENT,
    `festival_id` int,
    `url`         varchar(200),
    `created_at`  timestamp DEFAULT NOW()
);



Select *
from festival_information_like;
DROP TABLE IF EXISTS `festival_information_like`;
CREATE TABLE `festival_information_like`
(
    `id`          int PRIMARY KEY AUTO_INCREMENT,
    `festival_id` int,
    `user_id`     int,
    `created_at`  timestamp DEFAULT NOW()
);
