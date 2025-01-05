package com.yolo.mrs.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import com.yolo.mrs.mapper.UsersMapper;
import com.yolo.mrs.model.DTO.BlogDTO;
import com.yolo.mrs.model.PO.Blog;
import com.yolo.mrs.mapper.BlogMapper;
import com.yolo.mrs.model.PO.BlogContent;
import com.yolo.mrs.model.PO.Users;
import com.yolo.mrs.model.Result;
import com.yolo.mrs.service.IBlogContentService;
import com.yolo.mrs.service.IBlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yolo
 * @since 2025-01-05
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements IBlogService {

    @Resource
    UsersMapper usersMapper;
    @Resource
    IBlogContentService blogContentService;

    @Override
    public Result saveBlog(BlogDTO blogDTO) {
        //1,通过username查询userId
        Users user = usersMapper.selectByUserName(blogDTO.getUsername());
        //2，保存blog_content
        //2.1设置内容到blog_content
        BlogContent blogContent = new BlogContent();
        blogContent.setBlogContent(blogDTO.getContents());
        //2.2随机生成bolg_content_id，并设置
        UUID blog_content_id = UUID.randomUUID();
        blogContent.setBlogContentId(blog_content_id.toString());
        boolean saved = blogContentService.save(blogContent);
        if (!saved) {
            return Result.fail("文章内容保存失败");
        }
        //3,保存blog
        Blog blog = BeanUtil.copyProperties(blogDTO, Blog.class);
        blog.setAuthorId(user.getUserId());
        blog.setBlogContentId(blog_content_id.toString());
        blog.setCreateTime(blogDTO.getCreateTime());
        System.out.println("createTime"+blogDTO.getCreateTime());
        boolean saved1 = save(blog);
        if (!saved1) {
            return Result.fail("blog保存失败");
        }
        return Result.ok();
    }
}
