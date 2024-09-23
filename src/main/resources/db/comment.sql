create table comment (
    id int auto_increment primary key comment '主键',
    user_id varchar(30) not null comment '用户id',
    movie_id varchar(30) not null comment '电影id',
    content_id varchar(30) not null comment '评论内容id',
    create_time timestamp default current_timestamp comment '评论时间',
    status varchar(30) not null default '1' comment '状态',
    update_time timestamp default current_timestamp ON UPDATE current_timestamp,
    unique index idx_movie_id (movie_id),
    unique index idx_user_id (user_id)
)