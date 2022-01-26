package com.kimi.kel.core.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kimi.kel.core.listener.ExcelDefaultWordDTOListener;
import com.kimi.kel.core.listener.ExcelInitialWordDTOListener;
import com.kimi.kel.core.pojo.dto.ExcelInitialWordDTO;
import com.kimi.kel.core.pojo.entities.DefaultWord;
import com.kimi.kel.core.mapper.DefaultWordMapper;
import com.kimi.kel.core.service.DefaultWordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author kimi
 * @since 2022-01-23
 */
@Slf4j
@Service
public class DefaultWordServiceImpl extends ServiceImpl<DefaultWordMapper, DefaultWord> implements DefaultWordService {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private DefaultWordMapper defaultWordMapper;


    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void CompareAndImportData(InputStream inputStream) {
        EasyExcel.read(inputStream, ExcelInitialWordDTO.class, new ExcelDefaultWordDTOListener(redisTemplate, defaultWordMapper)).sheet().doRead();
        log.info("Excel导入成功");
    }

    @Override
    public List<DefaultWord> listByParentId(long parentId) {
        try {
            //首先查询redis中查询是否存在数据列表
            List<DefaultWord> defaultWords = (List<DefaultWord>) redisTemplate.opsForValue().get("kel:core:defaultWordList:" + parentId);
            if (defaultWords != null) {
                //如果存在则从redis中直接返回数据列表
                log.info("从redis中获取数据列表");
                return defaultWords;
            }
        } catch (Exception e) {
            log.error("redis服务器异常" + ExceptionUtils.getStackTrace(e));
        }

        //如果不存在则查询数据库
        log.info("从数据库中获取数据列表");
        QueryWrapper<DefaultWord> defaultWordQueryWrapper = new QueryWrapper<DefaultWord>();
        defaultWordQueryWrapper.eq("parent_id", parentId);
        List<DefaultWord> defaultWordList = baseMapper.selectList(defaultWordQueryWrapper);
        //填充hasChildren字段
        defaultWordList.forEach(defaultWord -> {
            //判断当前节点是否有子节点,找到当前的defaultWord下有没有子节点
            boolean hasChildren = this.hasChildren(defaultWord.getId());
            defaultWord.setHasChildren(hasChildren);
        });
        try {
            //将数据存入redis
            log.info("将数据存入redis");
            redisTemplate.opsForValue().set("kel:core:defaultWordList:" + parentId, defaultWordList, 5, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.error("redis服务器异常" + ExceptionUtils.getStackTrace(e));
        }
        //返回数据列表
        return defaultWordList;
    }

    /**
     * 判断当前id所在的节点下是否有子节点
     *
     * @param id
     * @return
     */
    private boolean hasChildren(Long id) {
        QueryWrapper<DefaultWord> defaultWordQueryWrapper = new QueryWrapper<DefaultWord>();
        defaultWordQueryWrapper.eq("id", id);
        Integer count = baseMapper.selectCount(defaultWordQueryWrapper);
        if (count.intValue() > 0) {
            return true;
        } else {
            return false;
        }
    }




}
