create table reply(
    id int auto_increment primary key comment '主键',
    comment_id varchar(30) not null comment '父评论id',
    A_user varchar(30) not null comment '评论者id',
    B_user varchar(30) not null comment '被评论者id',
    create_time timestamp default current_timestamp comment '评论的时间',
    update_time timestamp default current_timestamp on update current_timestamp,
    content_id varchar(30) not null comment '评论内容id',
    status varchar(30) not null default '1' comment '状态'
)