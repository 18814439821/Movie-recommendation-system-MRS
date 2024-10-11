create table comment
(
    id          int auto_increment primary key comment '主键',
    comment_id  varchar(255) not null comment '评论信息id',
    user_id     varchar(30)  not null comment '用户id',
    movie_id    varchar(30)  not null comment '电影id',
    content_id  varchar(255)  not null comment '评论内容id',
    create_time timestamp             default current_timestamp  comment '评论时间',
    status      varchar(30)  not null default '0' comment '状态,0-审核中，1-审核通过，2-审核不通过',
    update_time timestamp             default current_timestamp ON UPDATE current_timestamp,
    index idx_movie_id (movie_id),
    index idx_user_id (user_id),
    index idx_comment_id (comment_id)
)