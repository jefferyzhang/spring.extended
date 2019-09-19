package com.springextended.core.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * <p>
 *
 * </p>
 *
 * @author jefferyzhang
 * Email 343256635@qq.com
 * created at 2019 - 05 - 16 19:38
 */
@Component
public class JsonConvert {
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 序列化
     * @param object
     * @return
     */
    public String serializeObject(Object object){
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 反序列化
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T deserializeObject(String json,Class<T> clazz){
        try {
            return objectMapper.readValue(json,clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
