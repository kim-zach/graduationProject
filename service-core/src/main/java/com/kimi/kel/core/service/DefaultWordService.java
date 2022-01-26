package com.kimi.kel.core.service;

import com.kimi.kel.core.pojo.entities.DefaultWord;
import com.baomidou.mybatisplus.extension.service.IService;

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
}
