package com.kimi.kel.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kimi.kel.core.client.VodUploadVideoClient;
import com.kimi.kel.core.mapper.UserInfoMapper;
import com.kimi.kel.core.mapper.VideoCommentMapper;
import com.kimi.kel.core.pojo.entities.Video;
import com.kimi.kel.core.mapper.VideoMapper;
import com.kimi.kel.core.pojo.entities.VideoComment;
import com.kimi.kel.core.pojo.entities.WordVocabularyNotebook;
import com.kimi.kel.core.pojo.query.VideoInfoQuery;
import com.kimi.kel.core.pojo.vo.VideoDetailsVO;
import com.kimi.kel.core.pojo.vo.VideoInfoVO;
import com.kimi.kel.core.service.VideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author kimi
 * @since 2022-02-18
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {

    public static final int LIKE = 0;
    @Resource
    private VodUploadVideoClient vodUploadVideoClient;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private VideoCommentMapper videoCommentMapper;

    @Resource
    private VideoMapper videoMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveVideo(VideoInfoVO videoInfoVO) {

        Video video = new Video();

        video.setVideoSourceId(videoInfoVO.getVideoSourceId());
        video.setUserId(videoInfoVO.getUserId());
        video.setBrief(videoInfoVO.getBrief());
        video.setTitle(videoInfoVO.getTitle());
        video.setTag(videoInfoVO.getTag());

        int insert = baseMapper.insert(video);
        return insert > 0;
    }

    @Override
    public IPage<Video> getVideoListByUserId(Page<Video> videoPage, String userId) {

        QueryWrapper<Video> videoInfoVOQueryWrapper = new QueryWrapper<>();
        videoInfoVOQueryWrapper.eq("user_id", userId);

        return baseMapper.selectPage(videoPage, videoInfoVOQueryWrapper);

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteVideo(Long videoId, String videoSourceId) {

        int deleteById = baseMapper.deleteById(videoId);
        boolean deleteVideo = false;
        if (deleteById > 0) {
            deleteVideo = vodUploadVideoClient.deleteVideo(videoSourceId);
        }
        return deleteVideo;
    }

    @Override
    public IPage<Video> getVideoPageList(Page<Video> videoPage, VideoInfoQuery videoInfoQuery) {

        Integer status = videoInfoQuery.getStatus();
        String title = videoInfoQuery.getTitle();
        String tag = videoInfoQuery.getTag();
        String userId = videoInfoQuery.getUserId();


        QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq(status != null, "status", status)
                .like(StringUtils.isNoneBlank(title), "title", title)
                .like(StringUtils.isNoneBlank(tag), "tag", tag)
                .like(userId != null, "user_id", userId);


        return baseMapper.selectPage(videoPage, videoQueryWrapper);
    }

    @Override
    public VideoDetailsVO getVideoDetailsVOById(Long videoId) {

        Video video = baseMapper.selectById(videoId);

        VideoDetailsVO videoDetailsVO = new VideoDetailsVO();

        //上传视频作者名字
        String userName = userInfoMapper.getUserName(video.getUserId());
        videoDetailsVO.setUserName(userName);

        String videoSourceId = video.getVideoSourceId();
        //上传视频云id
        videoDetailsVO.setVideoSourceId(videoSourceId.substring(1, videoSourceId.length() - 1));

        //设置播放凭证
        String playAuthByVideoSourceId = vodUploadVideoClient.getPlayAuthByVideoSourceId(videoSourceId);
        videoDetailsVO.setPlayAuth(playAuthByVideoSourceId.substring(1, playAuthByVideoSourceId.length() - 1));

        videoDetailsVO.setUserId(video.getUserId());
        videoDetailsVO.setTitle(video.getTitle());
        videoDetailsVO.setBrief(video.getBrief());
        videoDetailsVO.setTag(video.getTag());
        videoDetailsVO.setClickAmount(video.getClickAmount());
        videoDetailsVO.setCollectAmount(video.getCollectAmount());
        videoDetailsVO.setViewAmount(video.getViewAmount());

        //视频评论
        List<VideoComment> comments = videoCommentMapper.selectByVideoId();
        videoDetailsVO.setComments(comments);

        return videoDetailsVO;
    }

    @Override
    public IPage<Video> getVideoListApproved(Page<Video> videoPage, Integer status, String searchObj) {

        QueryWrapper<Video> videoInfoVOQueryWrapper = new QueryWrapper<>();
        videoInfoVOQueryWrapper
                .eq("status", status)
                .like(StringUtils.isNoneBlank(searchObj),"tag",searchObj)
                .or()
                .like(StringUtils.isNoneBlank(searchObj),"brief",searchObj)
                .or()
                .like(StringUtils.isNoneBlank(searchObj),"title",searchObj)
                .orderByDesc("collect_amount");

        return baseMapper.selectPage(videoPage, videoInfoVOQueryWrapper);
    }

    @Override
    public boolean addViewAmount(Long videoId) {

        boolean result =  videoMapper.updateViewAmount(videoId);
        return result;
    }

    @Override
    public boolean likeOrDisLikeVideo(Long videoId, Integer favor) {
        boolean result = false;
        if(favor == LIKE){
            result  = videoMapper.increaseClickAmount(videoId);
            return result;
        }else{
            result = videoMapper.decreaseClickAmount(videoId);
            return result;
        }

    }


}
