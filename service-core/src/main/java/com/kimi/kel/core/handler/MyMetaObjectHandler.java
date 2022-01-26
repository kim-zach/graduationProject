package com.kimi.kel.core.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {

        log.info("insert 自动填充....");
        //实现填充业务逻辑
        this.strictInsertFill(metaObject,"createTime", LocalDateTime.class,LocalDateTime.now());
        this.strictUpdateFill(metaObject,"updateTime", LocalDateTime.class,LocalDateTime.now());


        //判断当前对象的自动填充属性是否包含当前属性
        boolean author = metaObject.hasSetter("author");
        if(author){
            log.info("insert author 属性");
            this.strictInsertFill(metaObject,"author",String.class,"kimi");
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("update 自动填充....");

        this.strictUpdateFill(metaObject,"updateTime", LocalDateTime.class,LocalDateTime.now());


    }
}
