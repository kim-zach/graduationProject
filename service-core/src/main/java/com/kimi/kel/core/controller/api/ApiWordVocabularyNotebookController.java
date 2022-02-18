package com.kimi.kel.core.controller.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kimi.common.exception.Assert;
import com.kimi.common.result.R;
import com.kimi.common.result.ResponseEnum;
import com.kimi.kel.core.pojo.entities.UserInfo;
import com.kimi.kel.core.pojo.entities.WordVocabularyNotebook;
import com.kimi.kel.core.pojo.vo.CardLearningVO;
import com.kimi.kel.core.pojo.vo.NoteBookWordVO;
import com.kimi.kel.core.service.WordVocabularyNotebookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api("生词本")
@Slf4j
@RestController
@RequestMapping("/api/core/wordVocabularyNotebook")
public class ApiWordVocabularyNotebookController {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private WordVocabularyNotebookService wordVocabularyNotebookService;

    @ApiOperation("添加生词")
    @PostMapping("/addNewWord")
    public R addNewWord(

            @ApiParam(value = "生词",required = true)
            @RequestBody CardLearningVO cardLearningVO
            ){

        //查找缓存，如果单词已经添加则返回错误信息
        if(redisTemplate.opsForSet().isMember("kel:core:addedSet"+cardLearningVO.getUserId(),cardLearningVO.getWordSpell())){
            return R.error().message("单词已经添加到生词表");
        }
        boolean result = wordVocabularyNotebookService.saveByUserId(cardLearningVO);

        if(result){
            return R.ok().message("添加成功");
        }else{
            return R.error().message("添加失败");
        }

    }


    @ApiOperation("获取生词本")
    @GetMapping("/getWordBookList/{page}/{limit}/{userId}")
    public R getWordBookList(
            @ApiParam(value = "当前页码", required = true)
            @PathVariable Long page ,
            @ApiParam(value = "每页记录数", required = true)
            @PathVariable Long limit,
            @ApiParam(value = "用户id",required = true)
            @PathVariable String userId
    ){

        Page<WordVocabularyNotebook> noteBookWordVOPage = new Page<>(page,limit);
        IPage<WordVocabularyNotebook> pageModel = wordVocabularyNotebookService.getNoteBookByUserId(noteBookWordVOPage,userId);

        log.info("pageModel:{}",pageModel);
        return R.ok().data("pageModel",pageModel);
    }


    @Transactional(rollbackFor = Exception.class)
    @ApiOperation("单词移出生词本")
    @GetMapping("/deleteItem/{wordId}")
    public R deleteItem(
            @ApiParam(value = "单词id",required = true)
            @PathVariable Long wordId
    ){
        //判断wordId不为空
        Assert.notNull(wordId, ResponseEnum.WORD_ID_NOT_NULL_ERROR);
        //查出wordSpell字段方便删缓存
        WordVocabularyNotebook wordToDelete = wordVocabularyNotebookService.getById(wordId);
        String wordSpell = null;
        Long userId = null;
        if(wordToDelete != null){
         wordSpell = wordToDelete.getWordSpell();
             userId = wordToDelete.getUserId();
        }

        boolean result = wordVocabularyNotebookService.removeById(wordId);
        if(result){
            //同时删除缓存中的单词，方便下次添加
            redisTemplate.opsForSet().remove("kel:core:addedSet" + userId,wordSpell);
            return R.ok().message("删除成功");
        }else{
            return R.error().message("删除失败");
        }
    }

}
