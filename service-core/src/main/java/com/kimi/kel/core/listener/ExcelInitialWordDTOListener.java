package com.kimi.kel.core.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.kimi.kel.core.mapper.InitialWordMapper;
import com.kimi.kel.core.pojo.dto.ExcelInitialWordDTO;
import com.kimi.kel.core.pojo.entities.DefaultWord;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@NoArgsConstructor
public class ExcelInitialWordDTOListener extends AnalysisEventListener<ExcelInitialWordDTO> {


    private RedisTemplate redisTemplate;

    private InitialWordMapper initialWordMapper;

    List<ExcelInitialWordDTO> list = new ArrayList<ExcelInitialWordDTO>();

    //每隔5条记录批量存储一次数据
    private static final int BATCH_COUNT = 5;

    public ExcelInitialWordDTOListener(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void invoke(ExcelInitialWordDTO data, AnalysisContext analysisContext) {

//        将数据存入数据列表
        list.add(data);

        if(list.size() >= BATCH_COUNT){
//            saveData();
            saveWordToInitialSet(list);
            list.clear();
        }
    }

    private void saveData() {
//        log.info("{}条数据被存储到数据库......", list.size());
        //调用mapper层的save方法 save list对象

        initialWordMapper.insertBatch(list);

//        log.info("{}条数据被存储到数据库成功", list.size());
    }

    private void saveWordToInitialSet(List<ExcelInitialWordDTO> list) {
        //将单词存入Initial set中,用于与default set做比较，从而决定添加到数据库的数据与修改tag的数据
        Iterator<ExcelInitialWordDTO> iterator = list.iterator();
        while (iterator.hasNext()) {
            ExcelInitialWordDTO initialItem = iterator.next();
            if(initialItem.getWordSpell() != null) {
                redisTemplate.opsForSet().add("kel:core:InitialWordSet", initialItem.getWordSpell());
            }
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        //当最后剩余的数据记录数不足BATCH_COUNT时，我们最终一次性存储剩余数量
//        saveData();
        saveWordToInitialSet(list);
        log.info("所有记录解析完成");

    }
}
