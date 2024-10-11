create table reply
(
    id          int auto_increment primary key comment '主键',
    reply_id    varchar(255) not null comment '回复信息id',
    comment_id  varchar(255)  not null comment '父评论id',
    A_user_id      varchar(30)  not null comment '评论者id',
    B_user_id      varchar(30)  not null comment '被评论者id',
    create_time timestamp             default current_timestamp comment '评论的时间',
    update_time timestamp             default current_timestamp on update current_timestamp,
    content_id  varchar(255)  not null comment '评论内容id',
    status      varchar(30)  not null default '0' comment '状态,0-审核中，1-审核通过，2-审核不通过',
    index idx_reply_id (reply_id)
)