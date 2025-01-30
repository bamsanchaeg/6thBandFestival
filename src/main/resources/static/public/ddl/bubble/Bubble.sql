SELECT * FROM bubble_post;
DROP TABLE IF EXISTS bubble_post;
CREATE TABLE bubble_post
(
    `id`                         int PRIMARY KEY AUTO_INCREMENT,
    `user_id`                    int,
    `title`                      varchar(100),
    `content`                    text,
    `created_at`                 timestamp DEFAULT NOW()
);

# ALTER TABLE bubble_post CHANGE user_id int;


SELECT * FROM bubble_images;
DROP TABLE IF EXISTS bubble_images;
CREATE TABLE bubble_images
(
    `id`                         int PRIMARY KEY AUTO_INCREMENT,
    `post_id`                    int,
    `image_url`                  varchar(200),
    `created_at`                 timestamp DEFAULT NOW()
);

SELECT * FROM bubble_like;
DROP TABLE IF EXISTS bubble_like;
CREATE TABLE bubble_like
(
    `id`                         int PRIMARY KEY AUTO_INCREMENT,
    `post_id`                    int,
    `user_id`                    int,
    `created_at`                 timestamp DEFAULT NOW()
);



SELECT * FROM bubble_follow;
DROP TABLE IF EXISTS bubble_follow;
CREATE TABLE bubble_follow
(
    `id`                         int PRIMARY KEY AUTO_INCREMENT,
    `following_id`               int,
    `follower_id`                int,
    `create_at` timestamp DEFAULT NOW()
);

# ALTER TABLE bubble_follow CHANGE artist_id following_id int;
# ALTER TABLE bubble_follow CHANGE user_id follower_id int;
# ALTER TABLE bubble_follow ADD create_at timestamp DEFAULT NOW();

# ALTER TABLE six_user ADD profile_img varchar(500);



##아티스트 신청테이블. 이걸로 새로 아티스트 ACCESS 신청함
SELECT * FROM bubble_artist;
DROP TABLE IF EXISTS bubble_artist;
CREATE TABLE bubble_artist
(
    `id`                         int PRIMARY KEY AUTO_INCREMENT,
    `user_id`                    int,
    `user_name`                  Varchar(200),
    `identification_image`       Varchar(500) NULL,
    `access_statement`           varchar(1) DEFAULT 'N',
    `email_address`              varchar(500),
    `created_at`                  timestamp DEFAULT NOW()
);

# ALTER TABLE bubble_artist MODIFY access_statement varchar(1) DEFAULT 'N';
# ALTER TABLE bubble_artist CHANGE create_at created_at  timestamp DEFAULT NOW();
# ALTER TABLE bubble_artist ADD user_name Varchar(200);
# ALTER TABLE bubble_artist ADD identification_image Varchar(500);
# ALTER TABLE bubble_artist ADD create_at timestamp DEFAULT NOW();


SELECT * FROM bubble_messages;
DROP TABLE IF EXISTS bubble_messages;
CREATE TABLE bubble_messages
(
    `id`                         int PRIMARY KEY AUTO_INCREMENT,
    `sender`                     int,
    `receiver`                   int,
    `content`                    text,
    `created_at`                 timestamp DEFAULT NOW(),
    `read_or_not`                CHAR(1) DEFAULT 'N'
);

# ALTER TABLE bubble_messages
#     MODIFY COLUMN read_or_not CHAR(1) DEFAULT 'N';
# ALTER TABLE bubble_messages DROP COLUMN chat_id;
# ALTER TABLE bubble_messages CHANGE artist_id sender int;
# ALTER TABLE bubble_messages CHANGE update_at read_or_not int;

SELECT * FROM bubble_comment;
DROP TABLE IF EXISTS bubble_comment;
CREATE TABLE bubble_comment
(
    `id`                         int PRIMARY KEY AUTO_INCREMENT,
    `post_id`                   int,
    `user_id`                   int,
    `content`                   varchar(1000),
    `created_at`                timestamp DEFAULT NOW()

)




