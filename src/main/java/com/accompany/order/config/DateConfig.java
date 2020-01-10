package com.accompany.order.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import java.util.Date;

/**
 * 日期转化类，将前端传递过来的时间戳转化为Date对象
 */
@Configuration
public class DateConfig implements Converter<String, Date> {
    @Override
    public Date convert(String s) {
        Long timeStamp = Long.parseLong(s);
        return new Date(timeStamp);
    }
}
