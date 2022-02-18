package com.kimi.kel.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kimi.common.exception.Assert;
import com.kimi.common.result.R;
import com.kimi.common.result.ResponseEnum;
import com.kimi.kel.core.mapper.WordVocabularyNotebookMapper;
import com.kimi.kel.core.pojo.entities.WordVocabularyNotebook;
import com.kimi.kel.core.pojo.vo.CardLearningVO;
import com.kimi.kel.core.pojo.vo.NoteBookWordVO;
import com.kimi.kel.core.service.WordVocabularyNotebookService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class WordVocabularyNotebookServiceImpl extends ServiceImpl<WordVocabularyNotebookMapper, WordVocabularyNotebook> implements WordVocabularyNotebookService {


    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public boolean saveByUserId( CardLearningVO cardLearningVO) {

        Assert.notNull(cardLearningVO,ResponseEnum.WORD_NOT_NULL_ERROR);
        Assert.notNull(cardLearningVO.getUserId(),ResponseEnum.WEIXIN_FETCH_USERINFO_ERROR);


        WordVocabularyNotebook wordVocabularyNotebook1 = new WordVocabularyNotebook();

        wordVocabularyNotebook1.setUserId(cardLearningVO.getUserId());
        wordVocabularyNotebook1.setWordSpell(cardLearningVO.getWordSpell());
        wordVocabularyNotebook1.setDescription(cardLearningVO.getDescription());
        wordVocabularyNotebook1.setTag(cardLearningVO.getTag());

        int insert = baseMapper.insert(wordVocabularyNotebook1);

        if(insert > 0){
            //添加单词到单词本成功，添加单词到用户已添加单词列表缓存中
            redisTemplate.opsForSet().add("kel:core:addedSet"+cardLearningVO.getUserId(),cardLearningVO.getWordSpell());
        }

        return insert > 0;
    }

    @Override
    public IPage<WordVocabularyNotebook> getNoteBookByUserId(Page<WordVocabularyNotebook> noteBookWordVOPage, String userId) {

        QueryWrapper<WordVocabularyNotebook> noteBookWordVOQueryWrapper = new QueryWrapper<>();
        noteBookWordVOQueryWrapper.eq("user_id",userId);

        return baseMapper.selectPage(noteBookWordVOPage, noteBookWordVOQueryWrapper);
    }
}
