package com.kimi.kel.core.controller;


import com.alibaba.excel.EasyExcel;
import com.kimi.common.exception.BusinessException;
import com.kimi.common.result.R;
import com.kimi.common.result.ResponseEnum;
import com.kimi.kel.core.pojo.dto.ExcelInitialWordDTO;
import com.kimi.kel.core.pojo.entities.DefaultWord;
import com.kimi.kel.core.service.DefaultWordService;
import com.kimi.kel.core.service.InitialWordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * <p>
 * 数据字典 前端控制器
 * </p>
 *
 * @author kimi
 * @since 2021-08-10
 */
@Api(tags = "default word management")
@Slf4j
//@CrossOrigin
@RestController
@RequestMapping("/admin/core/wordGather")
public class AdminInitialWordController {

    @Resource
    InitialWordService initialWordService;

    @Resource
    DefaultWordService defaultWordService;

    @ApiOperation("Excel数据的批量导入")
    @PostMapping("/import")
    public R batchImport(
            @ApiParam(value = "Excel数据字典文件", required = true)
            @RequestParam("file") MultipartFile file){

        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            initialWordService.importData(inputStream);
            //比较initialWord跟defaultWord，决定插入defaultWord与修改的数据
            log.info("success set data into set");
            inputStream =  file.getInputStream();
            defaultWordService.CompareAndImportData(inputStream);

            return R.ok().message("数据字典批量导入成功");

        } catch (Exception e) {
            throw new BusinessException(ResponseEnum.UPLOAD_ERROR,e);
        }
    }



    /**
     * 文件下载（失败了会返回一个有部分数据的Excel）
     * <p>
     * 1. 创建excel对应的实体对象
     * <p>
     * 2. 设置返回的 参数
     * <p>
     * 3. 直接写，这里注意，finish的时候会自动关闭OutputStream,当然你外面再关闭流问题不大
     */
    @ApiOperation("Excel数据的导出")
    @GetMapping("/export/{tag}")
    public void download(
            @PathVariable(value = "tag") String tag,
            HttpServletResponse response) throws IOException {
        // 使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("default_word", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), ExcelInitialWordDTO.class).sheet("initial_word").doWrite(initialWordService.listInitialWordDataByTag(tag));

    }


    /*树形数据的两种加载方式
    方式一：非延迟加载
    需要后端返回的数据结构中包含嵌套数据，并且嵌套数据放在children属性中

    方式二：延迟加载
    不需要后端返回数据中包含嵌套数据，并且要定义布尔属性的hasChildren，表示当前节点是否包含子数据
    如果hasChildren为true，就表示当前节点包含子数据
    如果hasChildren为false，就表示当前节点不包含子数据
    如果当前节点包含数据，那么点击当前节点的时候，就需要通过load方法加载子数据
     */
    @ApiOperation("根据上级Id获取子节点列表")
    @GetMapping("/listByParentId/{parentId}")
    public R listByParentId(
            @ApiParam(value = "上级节点Id",required = true)
            @PathVariable long parentId
                            ){
        List<DefaultWord> dictList = defaultWordService.listByParentId(parentId);
        return R.ok().data("list",dictList);

    }
}

