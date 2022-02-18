package com.kimi.kel.core.controller.api;


import com.kimi.common.result.R;
import com.kimi.kel.core.pojo.entities.DefaultWord;
import com.kimi.kel.core.pojo.vo.CardLearningVO;
import com.kimi.kel.core.service.DefaultWordService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author kimi
 * @since 2022-01-23
 */
@RestController
@RequestMapping("/api/core/defaultWord")
public class ApiDefaultWordController {

    @Resource
    private DefaultWordService defaultWordService;

    @Resource
    private RedisTemplate redisTemplate;

    @ApiOperation("随机生成卡片单词")
    @GetMapping("/randomWord/{tag}")
    public R randomWord(
            @ApiParam(value = "对应标签",required = true )
            @PathVariable String tag
    ){
       CardLearningVO cardLearningVO =  defaultWordService.searchWordsRandomly(tag);
       return R.ok().data("cardLearningVO",cardLearningVO);
    }

    @ApiOperation("按Id查找单词")
    @GetMapping("/getWordById/{id}")
    public R getWordById(
            @ApiParam(value = "单词id",required = true)
            @PathVariable Long id
    ){
        DefaultWord word = defaultWordService.getById(id);
        if(word != null){
            return R.ok().data("word",word);
        }
        else{
            return R.error().message("查找出错");
        }
    }

}

