package com.kimi.kel.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kimi.kel.core.pojo.dto.ExcelInitialWordDTO;
import com.kimi.kel.core.pojo.entities.DefaultWord;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.List;

/**
 * <p>
 * 数据字典 服务类
 * </p>
 *
 * @author kimi
 * @since 2021-08-10
 */
public interface InitialWordService extends IService<DefaultWord> {

    void importData(InputStream inputStream);


    List<ExcelInitialWordDTO> listInitialWordDataByTag(String tag);


//    List<DefaultWord> listByParentId(Long parentId);
//
//    List<DefaultWord> findByDictCode(String dictCode);
//
//    String getNameByParentDictCodeAndValue(String dictCode,Integer value);
}
