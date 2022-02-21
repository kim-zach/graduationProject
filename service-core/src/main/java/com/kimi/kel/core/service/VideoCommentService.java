package com.kimi.kel.core.service;

import com.kimi.kel.core.pojo.entities.VideoComment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kimi.kel.core.pojo.vo.CommentVO;
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
public interface VideoCommentService extends IService<VideoComment> {

    boolean saveComment(CommentVO commentVO);

    List<CommentVO> getCommentsByVideoId(Long videoId);


}
