package com.kimi.kel.core.service;

import com.kimi.kel.core.pojo.entities.VideoCollect;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kimi
 * @since 2022-02-21
 */
public interface VideoCollectService extends IService<VideoCollect> {


    List<Long> getVideoIdListByUserId(Long userId);
}
