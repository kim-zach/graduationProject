package com.kimi.kel.core.controller;


import com.kimi.common.exception.Assert;
import com.kimi.common.result.R;
import com.kimi.common.result.ResponseEnum;
import com.kimi.kel.core.pojo.entities.WordModules;
import com.kimi.kel.core.service.WordModulesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author kimi
 * @since 2022-01-26
 */
@Slf4j
@Api(value = "单词模块管理")
@CrossOrigin
@RestController
@RequestMapping("/admin/core/wordModules")
public class WordModulesController {
    
    @Resource
    private WordModulesService wordModulesService;

    @ApiOperation("单词模块列表")
    @GetMapping("/list")
    public R listAll(){

        log.info("this is log.info");
        log.warn("this is log.warn");
        log.error("this is log.error");

        List<WordModules> list = wordModulesService.list();
        return R.ok().data("list",list).message("获取列表成功");
    }

    @ApiOperation(value = "根据属性删除记录",notes = "逻辑删除数据记录")
    @DeleteMapping("/remove/{id}")
    public R removeById(
            @ApiParam(value = "数据id",example = "100",required = true)
            @PathVariable long id){
        boolean result = wordModulesService.removeById(id);
        if(result){
            return R.ok().message("删除成功");
        }
        else{
            return R.error().message("删除失败");
        }
    }

    @ApiOperation("新增单词模块")
    @PostMapping("/save")
    public R save(
            @ApiParam(value = "单词模块对象",required = true)
            @RequestBody WordModules WordModules){

//        if(WordModules.getBorrowAmount() == null){
//            throw new BusinessException(ResponseEnum.BORROW_AMOUNT_NULL_ERROR);
//        }

        Assert.notNull(WordModules.getTag(), ResponseEnum.WORD_TAG_NULL_ERROR);
        Assert.notNull(WordModules.getModuleName(), ResponseEnum.WORD_TAG_NULL_ERROR);

        boolean result = wordModulesService.save(WordModules);
        if(result){
            return R.ok().message("添加成功");
        }
        else{
            return R.error().message("添加失败");
        }
    }

    @ApiOperation("通过Id获取单词模块")
    @GetMapping("/get/{id}")
    public R getById(
            @ApiParam(value = "传入的id",required = true , example = "1")
            @PathVariable long id){
        WordModules WordModules = wordModulesService.getById(id);
        if(WordModules != null){
            return R.ok().data("record",WordModules).message("获取成功");
        }
        else{
            return R.error().message("获取单词模块失败");
        }
    }

    @ApiOperation("修改单词模块")
    @PutMapping("/update")
    public R updateById(
            @ApiParam(value = "单词模块对象",required = true)
            @RequestBody WordModules WordModules){
        boolean result = wordModulesService.updateById(WordModules);
        if(result){
            return R.ok().message("更新成功");
        }
        else{
            return R.error().message("更新失败");
        }
    }


}

