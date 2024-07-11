CREATE TABLE `user` (
	`id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`loginId` varchar(15) NOT NULL,
	`password` varchar(256) NOT NULL,
	`name` varchar(20) NOT NULL,
	`email` varchar(128) NOT NULL,
	`profileIamgeUrl` varchar(256) NULL,
	`createdAt` timestamp DEFAULT CURRENT_TIMESTAMP,
	`updatedAt` timestamp DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

drop table `user`;

CREATE TABLE `post` (
	`id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`userId` int NOT NULL,
	`content` text NULL,
	`imagePath` varchar(256) NOT NULL,
	`createdAt` timestamp DEFAULT CURRENT_TIMESTAMP,
	`updatedAt` timestamp DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `comment` (
	`id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `userId` int NOT NULL,
    `postId` int NOT NULL,
    `content` varchar(256) NOT NULL,
    `createdAt` timestamp DEFAULT CURRENT_TIMESTAMP,
	`updatedAt` timestamp DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 글 번호로 댓글을 조회하는 인덱스를 미리 만들어
ALTER TABLE `comment` ADD INDEX `comment_idx_1` (postId);

CREATE TABLE `like` (
	`postId` int NOT NULL,
    `userId` int NOT NULL,
    `createdAt` timestamp DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 유니크 복합키 등록
ALTER TABLE `like` ADD UNIQUE INDEX `idx_postId_userId` (`postId`, `userId`);