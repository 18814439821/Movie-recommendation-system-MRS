create table movies (
    movie_id INT AUTO_INCREMENT PRIMARY KEY comment '主键',
    movie_name VARCHAR(255) NOT NULL UNIQUE comment '电影名称',
    stars VARCHAR(10) comment '评分，10.0满分，新增可以没有评分' default '0.0',
    cover VARCHAR(255) NOT NULL comment '电影封面',
    witticism VARCHAR(255) comment '电影名言，妙言',
    release_date DATE comment '电影上映日期',
    carousel INT comment '是否轮播' default 0
)