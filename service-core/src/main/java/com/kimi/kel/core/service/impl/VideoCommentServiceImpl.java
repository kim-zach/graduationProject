package com.kimi.kel.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.kimi.kel.core.mapper.UserInfoMapper;
import com.kimi.kel.core.pojo.entities.UserInfo;
import com.kimi.kel.core.pojo.entities.VideoComment;
import com.kimi.kel.core.mapper.VideoCommentMapper;
import com.kimi.kel.core.pojo.vo.CommentVO;
import com.kimi.kel.core.service.VideoCommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.aspectj.weaver.ast.Var;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kimi
 * @since 2022-02-18
 */
@Service
public class VideoCommentServiceImpl extends ServiceImpl<VideoCommentMapper, VideoComment> implements VideoCommentService {

    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private VideoCommentMapper videoCommentMapper;

    @Override
    public boolean saveComment(CommentVO commentVO) {

        VideoComment videoComment = new VideoComment();
        videoComment.setVideoId(commentVO.getVideoId());
        videoComment.setUserId(commentVO.getUserId());
        videoComment.setContent(commentVO.getContent());

        int insert = baseMapper.insert(videoComment);

        return insert > 0;
    }

    @Override
    public List<CommentVO> getCommentsByVideoId(Long videoId) {

        QueryWrapper<VideoComment> videoCommentQueryWrapper = new QueryWrapper<>();
        videoCommentQueryWrapper.eq("video_id",videoId);
        List<VideoComment> comments = baseMapper.selectList(videoCommentQueryWrapper);

        List<CommentVO> commentVOList = new ArrayList<>();
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        Long userId = null;
        for(VideoComment v : comments){
            userId = v.getUserId();
            CommentVO commentVO = new CommentVO();
            if(userId !=null){
                UserInfo userInfo = userInfoMapper.selectOne(new QueryWrapper<UserInfo>().eq("id",userId));

                if(userInfo != null){
                commentVO.setAvatar(userInfo.getAvatar());
                commentVO.setUserName(userInfo.getNickName());}
            }
            commentVO.setVideoId(v.getVideoId());
            commentVO.setContent(v.getContent());
            commentVO.setId(v.getId());
            commentVO.setUserId(v.getUserId());

            commentVOList.add(commentVO);
        }


        return commentVOList;
    }



}
