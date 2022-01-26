package com.kimi.kel.core.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kimi.kel.core.mapper.DefaultWordMapper;
import com.kimi.kel.core.mapper.InitialWordMapper;
import com.kimi.kel.core.pojo.dto.ExcelInitialWordDTO;
import com.kimi.kel.core.pojo.entities.DefaultWord;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;

@Slf4j
@NoArgsConstructor
public class ExcelDefaultWordDTOListener extends AnalysisEventListener<ExcelInitialWordDTO> {


    public static final String KEL_CORE_INITIAL_WORD_SET = "kel:core:InitialWordSet";
    public static final String KEL_CORE_DEFAULT_WORD_SET = "kel:core:defaultWordSet";
    public static final String WORD_TO_UPDATE_SET = "wordToUpdateSet";
    public static final String WORD_TO_INSERT_SET = "wordToInsertSet";
    private RedisTemplate redisTemplate;

    private DefaultWordMapper defaultWordMapper;

    List<ExcelInitialWordDTO> insertList = new ArrayList<ExcelInitialWordDTO>();
    List<ExcelInitialWordDTO> updateList = new ArrayList<ExcelInitialWordDTO>();


    //每隔5条记录批量存储一次数据
    private static final int BATCH_COUNT = 5;

    public ExcelDefaultWordDTOListener(RedisTemplate redisTemplate,DefaultWordMapper defaultWordMapper) {
        this.redisTemplate = redisTemplate;
        this.defaultWordMapper = defaultWordMapper;
    }

    //由于redisTemplate是由构造方法传参在生成对象的时候带进来的，所以在加载类定义常量的时候为空。
//     Set InitialWordSet = redisTemplate.opsForSet().members("kel:core:InitialWordSet");
//     Set defaultWordSet = redisTemplate.opsForSet().members("kel:core:defaultWordSet");

    @Override
    public void invoke(ExcelInitialWordDTO data, AnalysisContext analysisContext) {

        //判断default表为不为空
//        ifRedisHasDefaultSet();
        //如果是需要插入的data
        Long insertWordSet = compareDataToInsert();
//        log.info("insertWordSet:{}",insertWordSet);
        if(redisTemplate.opsForSet().isMember(WORD_TO_INSERT_SET,data.getWordSpell())) {
            //将数据存入数据列表
            insertList.add(data);
        }
        if(insertList.size() >= BATCH_COUNT){
            saveData();
            insertList.clear();
        }

        //如果是需要更新的data
//        Set updateSet = compareDataToUpdate();
        Long updateWordSet = compareDataToUpdate();
//        log.info("updateWordSet:{}",updateWordSet);
        if(updateWordSet > 0 && redisTemplate.opsForSet().isMember(WORD_TO_UPDATE_SET,data.getWordSpell())) {
            //取出原来的tag
            QueryWrapper<DefaultWord> defaultWordQueryWrapper = new QueryWrapper<>();
            defaultWordQueryWrapper.eq("word_spell",data.getWordSpell());
            DefaultWord defaultWord = defaultWordMapper.selectOne(defaultWordQueryWrapper);
//            log.info("defaultWord:{}",defaultWord);
            data.setTag(defaultWord.getTag() + "," + data.getTag());
//            log.info("data.getTag():{}",data.getTag());
            //将数据存入数据列表
            updateList.add(data);
        }
        if(updateList.size() >= BATCH_COUNT){
            updateData();
            updateList.clear();
        }


    }

    private void updateData() {
        defaultWordMapper.updateBatch(updateList);
    }

    private void saveData() {
//        log.info("{}条数据被存储到数据库......", insertList.size());
        //调用mapper层的save方法 save list对象

        defaultWordMapper.insertBatch(insertList);

//        log.info("{}条数据被存储到数据库成功", insertList.size());
    }
    private void ifRedisHasDefaultSet(){
        Set members = redisTemplate.opsForSet().members(KEL_CORE_DEFAULT_WORD_SET);
        if(members.isEmpty()){
            Set<String> defaultWordSet = defaultWordMapper.selectWordSpell();
//            redisTemplate.opsForSet().add(KEL_CORE_DEFAULT_WORD_SET,defaultWordSet);
//            redisTemplate.opsForSet().differenceAndStore(defaultWordSet,KEL_CORE_DEFAULT_WORD_SET);
            redisTemplate.opsForSet().unionAndStore(defaultWordSet,KEL_CORE_DEFAULT_WORD_SET);
        }
    }


    private Long compareDataToInsert() {

            // 需要的是key的值，而不是整个set
//        Set InitialWordSet = redisTemplate.opsForSet().members("kel:core:InitialWordSet");
//        Set defaultWordSet = redisTemplate.opsForSet().members("kel:core:defaultWordSet");
        //比较defaultWordSet 与 InitialWordSet
        //差集

        return redisTemplate.opsForSet().differenceAndStore(KEL_CORE_INITIAL_WORD_SET,KEL_CORE_DEFAULT_WORD_SET, WORD_TO_INSERT_SET);

    }

    private Long compareDataToUpdate() {
        //交集
         return redisTemplate.opsForSet().intersectAndStore(KEL_CORE_INITIAL_WORD_SET, KEL_CORE_DEFAULT_WORD_SET,WORD_TO_UPDATE_SET);
    }

    private void mergeDataToDefaultSet() {

        //交集
         redisTemplate.opsForSet().unionAndStore(KEL_CORE_INITIAL_WORD_SET, KEL_CORE_DEFAULT_WORD_SET,KEL_CORE_DEFAULT_WORD_SET);
    }

    /*
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

     */

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        //当最后剩余的数据记录数不足BATCH_COUNT时，我们最终一次性存储剩余数量
        if(insertList.size() > 0) {
            saveData();
        }
        if(updateList.size() > 0 ) {
            updateData();
        }
        //合并新的defaultWordSet
        mergeDataToDefaultSet();
        log.info("所有记录解析完成");

    }
}
