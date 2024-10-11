package com.yolo.mrs.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.yolo.mrs.mapper.CommentContentMapper;
import com.yolo.mrs.mapper.ReplyMapper;
import com.yolo.mrs.mapper.UsersMapper;
import com.yolo.mrs.model.DTO.CommentDTO;
import com.yolo.mrs.model.DTO.CommentForm;
import com.yolo.mrs.model.DTO.ReplyDTO;
import com.yolo.mrs.model.PO.Comment;
import com.yolo.mrs.mapper.CommentMapper;
import com.yolo.mrs.model.PO.CommentContent;
import com.yolo.mrs.model.PO.Reply;
import com.yolo.mrs.model.PO.Users;
import com.yolo.mrs.model.Result;
import com.yolo.mrs.model.VO.CommentVO;
import com.yolo.mrs.model.VO.ReplyVO;
import com.yolo.mrs.model.VO.UsersVO;
import com.yolo.mrs.service.ICommentContentService;
import com.yolo.mrs.service.ICommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yolo.mrs.service.IReplyService;
import com.yolo.mrs.service.IUsersService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static com.yolo.mrs.utils.Constant.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yolo
 * @since 2024-09-23
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

    @Resource
    public ICommentContentService commentContentService;
    @Resource
    public IReplyService replyService;
    @Resource
    public CommentContentMapper commentContentMapper;
    @Resource
    private CommentMapper commentMapper;
    @Resource
    private IUsersService usersService;
    @Resource
    private ReplyMapper replyMapper;
    @Resource
    private UsersMapper usersMapper;

    /*添加评论*/
    @Override
    public Result addComment(CommentForm commentForm) {
        System.out.println("commentForm = " + commentForm);
        //获取到commentForm之后判断评论模式
        if (commentForm.getFlag().equals(COMMENT_MOD_ONE)){
            String comment_id = UUID.randomUUID().toString();
            Comment comment = new Comment();
            int i = saveComment(commentForm, comment, comment_id);
            if (i == 1){
                return Result.ok("添加成功");
            }else {
                return Result.fail("添加失败");
            }
        }else {
            String reply_id = UUID.randomUUID().toString();
            int i1 = saveReply(commentForm, reply_id);
            if (i1 == 1){
                return Result.ok("添加成功");
            }else {
                return Result.fail("添加失败");
            }
        }
    }

    /*获取评论*/
    @Override
    public Result getBasicComment(String movieId) {
        //根据movieId查询所有的根评论
        //判断是否有人评论，如果没有直接返回空
        List<Comment> commentList = commentMapper.getListByMovieId(movieId);
        if (commentList.isEmpty()){
            return null;
        }
        List<CommentDTO> commentDTOList = commentList.stream().map(comment -> BeanUtil.copyProperties(comment, CommentDTO.class)).toList();
        for (CommentDTO commentDTO : commentDTOList) {
            //查询评论内容
            commentDTO.setContent(commentContentMapper.getByContentId(commentDTO.getContentId()).getContent());
            //查询根评论用户信息
            Users users = usersService.getById(commentDTO.getUserId());
            commentDTO.setUserName(users.getUsername());
            commentDTO.setUserPhoto(users.getPhoto());
            //根据commentId在reply表中查询所有相关回复
            List<Reply> replies = replyMapper.selectByCommentId(commentDTO.getCommentId());
            List<ReplyDTO> replyDTOList = replies.stream().map(reply -> BeanUtil.copyProperties(reply, ReplyDTO.class)).toList();
            //判断是否有人回复，没有直接跳过不去查询
            if (!replies.isEmpty()){
                //查询replyVOList
                for (ReplyDTO replyDTO : replyDTOList) {
                    //1，查询回复内容
                    replyDTO.setContent(commentContentMapper.getByContentId(replyDTO.getContentId()).getContent());
                    //2.查询回复者信息（昵称，头像）
                    Users userA = usersService.getById(replyDTO.getAUserId());
                    replyDTO.setAUserName(userA.getUsername());
                    replyDTO.setAUserPhoto(userA.getPhoto());
                    //3.判断是否有被回复者，有就查询被回复者信息（昵称）
                    if (StrUtil.isNotBlank(replyDTO.getBUserId()) && StrUtil.isNotEmpty(replyDTO.getBUserId())){
                        Users userB = usersService.getById(replyDTO.getBUserId());
                        replyDTO.setBUserName(userB.getUsername());
                    }
                    //4.如果没有被回复者就直接跳过
                }
                //转为vo
                List<ReplyVO> replyVOList = replyDTOList.stream().map(replyDTO -> BeanUtil.copyProperties(replyDTO, ReplyVO.class)).toList();
                //放入replyVOList
                commentDTO.setReplyVOList(replyVOList);
            }
        }
        //转为vo
        List<CommentVO> commentVOList = commentDTOList.stream().map(commentDTO -> BeanUtil.copyProperties(commentDTO, CommentVO.class)).toList();
        return Result.ok(commentVOList);
    }

    //添加回复
    private int saveReply(CommentForm commentForm, String reply_id) {
        try {
            //保存到回复表
            Reply reply = new Reply();
            //添加replyId
            reply.setReplyId(reply_id);
            //添加回复的评论id
            reply.setCommentId(commentForm.getCommentId());
            //为了隐藏用户数量所以前端没有保存自增长的userId,我们需要后面去查询一下
            Users userA = usersMapper.selectByUserName(commentForm.getAUserName());
            //添加回复者id
            reply.setAUserId(String.valueOf(userA.getUserId()));
            //判断是否有被回复者，如果没有就说明是回复根评论
            if (commentForm.getFlag().equals(COMMENT_MOD_THREE)){
                //如果有被回复者就添加，说明是回复回复
                Users userB = usersMapper.selectByUserName(commentForm.getBUserName());
                //添加被回复者id
                reply.setBUserId(String.valueOf(userB.getUserId()));
            }
            reply.setContentId(reply_id);
            reply.setCreateTime(LocalDateTime.now());
            replyService.save(reply);
            //保存到评论内容表
            CommentContent commentContent = new CommentContent();
            commentContent.setContent(commentForm.getContent());
            commentContent.setContentId(reply_id);
            commentContentService.save(commentContent);
        } catch (Exception e) {
            return 0;
        }
        return 1;
    }

    //添加评论
    private int saveComment(CommentForm commentForm, Comment comment, String comment_id) {
        try {
            //保存到主评论表
            comment.setCommentId(comment_id);
            //为了隐藏用户数量所以前端没有保存自增长的userId,我们需要后面去查询一下
            Users users = usersMapper.selectByUserName(commentForm.getAUserName());
            comment.setUserId(String.valueOf(users.getUserId()));
            comment.setMovieId(commentForm.getMovieId());
            comment.setContentId(comment_id);
            comment.setCreateTime(LocalDateTime.now());
            save(comment);
            //保存到评论内容表
            CommentContent commentContent = new CommentContent();
            commentContent.setContentId(comment_id);
            commentContent.setContent(commentForm.getContent());
            commentContentService.save(commentContent);
        } catch (Exception e) {
            //添加失败，后续处理
            return 0;
        }
        return 1;
    }
}
