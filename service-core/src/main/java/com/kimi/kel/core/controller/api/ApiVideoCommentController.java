package com.kimi.kel.core.controller.api;


import com.kimi.common.exception.Assert;
import com.kimi.common.result.R;
import com.kimi.common.result.ResponseEnum;
import com.kimi.kel.core.pojo.entities.VideoComment;
import com.kimi.kel.core.pojo.vo.CommentVO;
import com.kimi.kel.core.service.VideoCommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author kimi
 * @since 2022-02-18
 */
@RestController
@RequestMapping("/api/core/videoComment")
public class ApiVideoCommentController {

    @Resource
    private VideoCommentService videoCommentService;

    @ApiOperation("用户对视频发表评论")
    @PostMapping("/submitComment")
    public R submitComment(
            @ApiParam(value = "用户评论视频对象",required = true)
            @RequestBody CommentVO commentVO
    ){
        boolean result = videoCommentService.saveComment(commentVO);
        if(result){
            return R.ok().message("评论发表成功");
        }
        return R.error().message("评论发表失败");
    }

    @ApiOperation("获取评论列表")
    @GetMapping("/getCommentsByVideoId/{videoId}")
    public R getCommentsByVideoId(
            @ApiParam(value = "视频id",required = true)
            @PathVariable Long videoId
    ){

        Assert.notNull(videoId,ResponseEnum.VIDEO_ID_NOT_NULL_ERROR);
        List<CommentVO> videoCommentList = videoCommentService.getCommentsByVideoId(videoId);
        if(!CollectionUtils.isEmpty(videoCommentList)){
            return R.ok().data("commentList",videoCommentList);
        }
        return R.ok();
    }

    @ApiOperation("删除自己的评论")
    @DeleteMapping("/deleteComment/{id}")
    public R deleteComment(
            @ApiParam(value = "commentId",required = true)
            @PathVariable Long id
    ){
        boolean remove = videoCommentService.removeById(id);
        if(remove){
            return R.ok().message("删除成功");
        }else{
            return R.error().message("删除失败");
        }
    }
}

