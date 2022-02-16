package com.kimi.kel.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kimi.kel.core.pojo.entities.DefaultWord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kimi.kel.core.pojo.vo.CardLearningVO;

import java.io.InputStream;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kimi
 * @since 2022-01-23
 */
public interface DefaultWordService extends IService<DefaultWord> {

    void CompareAndImportData(InputStream inputStream);

    List<DefaultWord> listByParentId(long parentId);

    boolean selectWordsByTag(String tag);

    IPage<DefaultWord> listWordsPageByTag(Page<DefaultWord> wordsPage, String tag);

    IPage<DefaultWord> searchWordsPageByTag(Page<DefaultWord> wordsPage, String wordSpell);

    CardLearningVO searchWordsRandomly(String tag);
}
