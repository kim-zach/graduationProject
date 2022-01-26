package com.kimi.kel.core.convertor;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import io.lettuce.core.Value;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;

@Slf4j
public class LocalDateTimeConverter implements Converter<LocalDateTime> {

    private static final String DEFAULT_PATTERN = "yyyy-MM-dd";
    @Override
    public Class supportJavaTypeKey() {
        return LocalDateTime.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public LocalDateTime convertToJavaData(CellData cellData, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
//        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DEFAULT_PATTERN);
//        LocalDate localDate = LocalDate.parse(cellData.getStringValue(),dateTimeFormatter);
//        LocalDateTime localDateTime = LocalDateTime.of(localDate, LocalTime.of(00,00));
//        return localDateTime;
        //文本格式
//        return LocalDateTime.parse(cellData.getStringValue(),DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        BigDecimal numberValue = cellData.getNumberValue();
        log.info("numberValue:{}" + numberValue);
        long second = numberValue.multiply(new BigDecimal("86400")).longValue();
        log.info("second:{}" + second);
        Instant instant = Instant.ofEpochSecond(second - 2209190400L);


        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    @Override
    public CellData convertToExcelData(LocalDateTime localDateTime, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return new CellData<>(localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
}
