package com.kimi.kel.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kimi.kel.core.pojo.entities.Video;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kimi.kel.core.pojo.query.VideoInfoQuery;
import com.kimi.kel.core.pojo.vo.VideoDetailsVO;
import com.kimi.kel.core.pojo.vo.VideoInfoVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kimi
 * @since 2022-02-18
 */
@Service
public interface VideoService extends IService<Video> {

    boolean saveVideo(VideoInfoVO videoInfoVO);

    IPage<Video> getVideoListByUserId(Page<Video> videoPage, String userId);

    boolean deleteVideo(Long videoId, String videoSourceId);

    IPage<Video> getVideoPageList(Page<Video> videoPage, VideoInfoQuery videoInfoQuery);

    VideoDetailsVO getVideoDetailsVOById(Long videoId);

    IPage<Video> getVideoListApproved(Page<Video> videoPage, Integer status,String searchObj);


    boolean addViewAmount(Long videoId);

    boolean likeOrDisLikeVideo(Long videoId, Integer favor,Long userId);

    boolean collectOrDiscollectVideo(Long videoId, Integer favor, Long userId);

    IPage<Video> getVideoDetailsByVideoIdList(Page<Video> videoPage, List<Long> videoIdList);
}
