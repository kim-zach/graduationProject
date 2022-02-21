package com.kimi.kel.core.mapper;

import com.kimi.kel.core.pojo.entities.VideoLike;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author kimi
 * @since 2022-02-21
 */
public interface VideoLikeMapper extends BaseMapper<VideoLike> {

    List<Long> getVideoIdListByUserId(Long userId);
}
