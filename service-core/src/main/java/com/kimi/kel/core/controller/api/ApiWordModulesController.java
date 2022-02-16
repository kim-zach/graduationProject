package com.kimi.kel.core.controller.api;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kimi.common.exception.Assert;
import com.kimi.common.result.R;
import com.kimi.common.result.ResponseEnum;
import com.kimi.kel.core.pojo.entities.DefaultWord;
import com.kimi.kel.core.pojo.entities.UserInfo;
import com.kimi.kel.core.pojo.entities.WordModules;
import com.kimi.kel.core.pojo.query.UserInfoQuery;
import com.kimi.kel.core.service.DefaultWordService;
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
@Api(value = "单词模块获取")
//@CrossOrigin
@RestController
@RequestMapping("/api/core/wordModules")
public class ApiWordModulesController {
    @Resource
    private DefaultWordService defaultWordService;

    @Resource
    private WordModulesService wordModulesService;

    @ApiOperation("获取单词模块列表")
    @GetMapping("/getModules")
    public R listAll(){

        log.info("this is log.info");
        log.warn("this is log.warn");
        log.error("this is log.error");

        List<String> list = wordModulesService.getModules();
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

        //如果tag存在的话，只更新数据不增加模块

        boolean ifExistModule = wordModulesService.ifExistModule(WordModules.getTag());
        boolean result = false;
        if(!ifExistModule){
            //不存在模块再添加模块，存在模块的话只更新模块相应的数据
            result = wordModulesService.save(WordModules);
        }
        if(result){
            return R.ok().message("添加成功");
        }
        else if(ifExistModule){
            return R.ok().message("模块已存在，只更新数据");
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


    @ApiOperation("存储选中tag相关单词到缓存中")
    @GetMapping("/getWordsByTag/{tag}")
    public R getWordsByTag(
            @PathVariable String tag){
        boolean result = defaultWordService.selectWordsByTag(tag);
        //将chosenSet中拿到的set到characterSet中
        //defaultWordService.setCharacterSet


        if(result){
            return R.ok().message("加载单词数据成功");
        }else{
            return R.error().message("加载单词数据失败");
        }
    }

    @ApiOperation("选取模块相关单词以列表展示给前端")
    @GetMapping("/listWordsPageByTag/{page}/{limit}/{tag}")
    public R listWordsPageByTag(
            @ApiParam(value = "当前页码", required = true)
            @PathVariable Long page ,
            @ApiParam(value = "每页记录数", required = true)
            @PathVariable Long limit,
            @ApiParam(value = "按标签查询", required = true)
            @PathVariable String tag){

        Page<DefaultWord> wordsPage = new Page<>(page,limit);
            IPage<DefaultWord> pageModel = defaultWordService.listWordsPageByTag(wordsPage,tag);

        return R.ok().data("pageModel",pageModel);
    }


    @ApiOperation("搜索单词")
    @GetMapping("/searchWordsPageByTag/{page}/{limit}/{wordSpell}")
    public R searchWordsPageByTag(
            @ApiParam(value = "当前页码", required = true)
            @PathVariable Long page ,
            @ApiParam(value = "每页记录数", required = true)
            @PathVariable Long limit,
            @ApiParam(value = "搜单词", required = true)
            @PathVariable String wordSpell){

        Page<DefaultWord> wordsPage = new Page<>(page,limit);
        IPage<DefaultWord> pageModel = defaultWordService.searchWordsPageByTag(wordsPage,wordSpell);

        return R.ok().data("pageModel",pageModel);
    }
}

