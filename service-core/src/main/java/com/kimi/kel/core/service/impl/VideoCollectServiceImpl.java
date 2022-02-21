package com.kimi.kel.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kimi.kel.core.pojo.entities.VideoCollect;
import com.kimi.kel.core.mapper.VideoCollectMapper;
import com.kimi.kel.core.service.VideoCollectService;
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
public class VideoCollectServiceImpl extends ServiceImpl<VideoCollectMapper, VideoCollect> implements VideoCollectService {

    @Resource
    private VideoCollectMapper videoCollectMapper;



    @Override
    public List<Long> getVideoIdListByUserId(Long userId) {
        List<Long> videoIdList = videoCollectMapper.getVideoIdListByUserId(userId);
        return videoIdList;
    }
}
