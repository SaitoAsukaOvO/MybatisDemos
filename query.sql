create table `user`(
                       `id` int(20) not null primary key,
                       `name` varchar(30) default null,
                       `pwd` varchar(30) default null
)engine=InnoDB default charset=utf8;

CREATE TABLE `teacher` (
                           `id` INT(10) NOT NULL,
                           `name` VARCHAR(30) DEFAULT NULL,
                           PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8

INSERT INTO teacher(`id`, `name`) VALUES (1, 'teacher1');

CREATE TABLE `student` (
                           `id` INT(10) NOT NULL,
                           `name` VARCHAR(30) DEFAULT NULL,
                           `tid` INT(10) DEFAULT NULL,
                           PRIMARY KEY (`id`),
                           KEY `fktid` (`tid`),
                           CONSTRAINT `fktid` FOREIGN KEY (`tid`) REFERENCES `teacher` (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8
INSERT INTO `student` (`id`, `name`, `tid`) VALUES ('1', '小明', '1');
INSERT INTO `student` (`id`, `name`, `tid`) VALUES ('2', '小红', '1');
INSERT INTO `student` (`id`, `name`, `tid`) VALUES ('3', '小张', '1');
INSERT INTO `student` (`id`, `name`, `tid`) VALUES ('4', '小李', '1');
INSERT INTO `student` (`id`, `name`, `tid`) VALUES ('5', '小王', '1');


CREATE TABLE `blog`(
                       `id` VARCHAR(50) NOT NULL COMMENT '博客id',
                       `title` VARCHAR(100) NOT NULL COMMENT '博客标题',
                       `author` VARCHAR(30) NOT NULL COMMENT '博客作者',
                       `create_time` DATETIME NOT NULL COMMENT '创建时间',
                       `views` INT(30) NOT NULL COMMENT '浏览量'
)ENGINE=INNODB DEFAULT CHARSET=utf8
