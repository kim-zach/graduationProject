package com.kimi.kel.core.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kimi.kel.core.listener.ExcelInitialWordDTOListener;
import com.kimi.kel.core.mapper.InitialWordMapper;
import com.kimi.kel.core.pojo.dto.ExcelInitialWordDTO;
import com.kimi.kel.core.pojo.entities.DefaultWord;
import com.kimi.kel.core.service.InitialWordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 数据字典 服务实现类
 * </p>
 *
 * @author kimi
 * @since 2021-08-10
 */
@Slf4j
@Service
public class InitialWordServiceImpl extends ServiceImpl<InitialWordMapper, DefaultWord> implements InitialWordService {

    @Resource
    private RedisTemplate redisTemplate;
//    @Resource
//    private DictMapper dictMapper;

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void importData(InputStream inputStream) {
        EasyExcel.read(inputStream, ExcelInitialWordDTO.class, new ExcelInitialWordDTOListener(redisTemplate)).sheet().doRead();
        log.info("Excel导入成功");
    }

    @Override
    public List<ExcelInitialWordDTO> listInitialWordDataByTag(String tag) {

        QueryWrapper<DefaultWord> defaultWordQueryWrapper = new QueryWrapper<>();
//        if(tag != null ){
        if(!"null".equals(tag)){
            defaultWordQueryWrapper.like("tag",tag);
        }
        List<DefaultWord> defaultWordList = baseMapper.selectList(defaultWordQueryWrapper);
        //创建ExcelDictDTO列表。将Dict列表转换成ExcelDictDTO列表
        ArrayList<ExcelInitialWordDTO> initialWordDTOList = new ArrayList<>(defaultWordList.size());
        defaultWordList.forEach(dict -> {
            ExcelInitialWordDTO initialWordDTO = new ExcelInitialWordDTO();
            BeanUtils.copyProperties(dict, initialWordDTO);
            initialWordDTOList.add(initialWordDTO);
        });

        return initialWordDTOList;
    }




//    @Override
//    public List<Dict> findByDictCode(String dictCode) {
//
//        QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();
//        dictQueryWrapper.eq("dict_code", dictCode);
//
//        Dict dict = baseMapper.selectOne(dictQueryWrapper);
//        return this.listByParentId(dict.getId());
//    }
//
//    @Override
//    public String getNameByParentDictCodeAndValue(String dictCode, Integer value) {
//
//        QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();
//        dictQueryWrapper.eq("dict_code",dictCode);
//        Dict parentDict = baseMapper.selectOne(dictQueryWrapper);
//
//        if(parentDict == null){
//            return "";
//        }
//
//        dictQueryWrapper = new QueryWrapper<>();
//        dictQueryWrapper.eq("parent_id",parentDict.getId()).eq("value",value);
//        Dict dict = baseMapper.selectOne(dictQueryWrapper);
//
//        if( dict == null){
//            return "";
//        }
//
//        return dict.getName();



//    }


}
