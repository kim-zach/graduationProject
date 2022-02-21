package com.kimi.kel.core.mapper;

import com.kimi.kel.core.pojo.entities.VideoComment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kimi.kel.core.pojo.vo.CommentVO;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author kimi
 * @since 2022-02-18
 */
public interface VideoCommentMapper extends BaseMapper<VideoComment> {

    List<VideoComment> selectByVideoId();

    List<CommentVO> getCommentsByVideoId(Long videoId);
}
