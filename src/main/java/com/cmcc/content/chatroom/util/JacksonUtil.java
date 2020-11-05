package com.cmcc.content.chatroom.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 一贫
 * @date 2020/11/5
 */
@Component
@Slf4j
public class JacksonUtil {

    @Autowired
    private ObjectMapper objectMapper;

    public String writeValueAsString(Object value) throws RuntimeException {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            String msg = String.format("Jackson转String失败,对象:{}", value);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    public <T> T readValue(String content, Class<T> valueType) throws RuntimeException {
        try {
            return objectMapper.readValue(content, valueType);
        } catch (JsonProcessingException e) {
            String msg = String.format("Jackson转换对象失败,String:{},Class:{}", content, valueType);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }
}
