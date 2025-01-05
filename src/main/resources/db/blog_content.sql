create table blog_content
(
    blog_content_id varchar(36) primary key not null comment '主键',
    blog_content text not null comment '文章内容'
)