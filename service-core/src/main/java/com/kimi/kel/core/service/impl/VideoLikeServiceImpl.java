package com.kimi.kel.core.service.impl;

import com.kimi.kel.core.pojo.entities.VideoLike;
import com.kimi.kel.core.mapper.VideoLikeMapper;
import com.kimi.kel.core.service.VideoLikeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kimi
 * @since 2022-02-21
 */
@Service
public class VideoLikeServiceImpl extends ServiceImpl<VideoLikeMapper, VideoLike> implements VideoLikeService {


    @Resource
    private VideoLikeMapper videoLikeMapper;

    @Override
    public List<Long> getVideoIdListByUserId(Long userId) {
        List<Long> videoIdList = videoLikeMapper.getVideoIdListByUserId(userId);
        return videoIdList;
    }
}
