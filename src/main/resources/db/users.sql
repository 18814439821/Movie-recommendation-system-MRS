CREATE TABLE users (
                       user_id INT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       email VARCHAR(255),
                       phone VARCHAR(20),
                       photo VARCHAR(255),  -- 存储的是图片的URL或文件路径
                       user_level ENUM('admin', 'vip', 'user') NOT NULL DEFAULT 'user',  -- 使用ENUM类型限制user_level的值
                       CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 自动记录创建时间
                       UPDATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP  -- 自动更新修改时间
);