package com.kimi.kel.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kimi.kel.core.pojo.entities.WordModules;
import com.kimi.kel.core.pojo.entities.WordVocabularyNotebook;
import com.kimi.kel.core.pojo.vo.CardLearningVO;
import com.kimi.kel.core.pojo.vo.NoteBookWordVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WordVocabularyNotebookService extends IService<WordVocabularyNotebook> {
    boolean saveByUserId(CardLearningVO cardLearningVO);

    IPage<WordVocabularyNotebook>  getNoteBookByUserId(Page<WordVocabularyNotebook> noteBookWordVOPage, String userId);
}
