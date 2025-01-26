create table movie_casts (
    movie_casts_id int comment '电影演员表id' primary key,
    movie_id int comment '电影ID',
    movie_casts varchar(255) comment '演员列表',
    foreign key (movie_id) references movies (movie_id)
)