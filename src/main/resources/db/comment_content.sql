create table comment_content
(
    id         int auto_increment primary key comment '主键',
    content_id varchar(255) not null comment '评论内容id',
    content    varchar(255) not null comment '评论内容',
    index idx_content_id (content_id)
)