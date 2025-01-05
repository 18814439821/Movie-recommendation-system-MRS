create table blog
(
    id              int auto_increment primary key comment '主键',
    title           varchar(50) not null comment '题目',
    movie_id        int         not null comment '绑定电影id',
    blog_content_id varchar(36)         not null comment '绑定文章内容id',
    author_id       int         not null comment '作者id',
    create_time     timestamp            default current_timestamp comment '编写时间',
    update_time     timestamp            default current_timestamp comment '修改时间' on update current_timestamp,
    status          int         not null default 0 comment '0-未审核，1-审核通过，2-审核不通过',
    foreign key (movie_id) references movies (movie_id),
    foreign key (author_id) references users (user_id),
    foreign key (blog_content_id) references blog_content (blog_content_id)
)