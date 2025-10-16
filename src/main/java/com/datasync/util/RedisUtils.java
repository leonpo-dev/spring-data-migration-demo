package com.datasync.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Redis 工具类（泛型版本）
 * 封装常用的Redis操作，支持对象JSON序列化
 * <p>
 * 特点：
 * - 支持任意类型缓存（泛型）
 * - 自动进行JSON序列化与反序列化
 * - 提供基础操作封装（set/get/incr/expire/del等）
 * - 线程安全、生产可用
 */
@Component
public class RedisUtils {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public RedisUtils(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = new ObjectMapper();
    }

    // ======================= 基础String操作 =======================

    /**
     * 存入字符串（永久有效）
     */
    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 存入字符串并设置过期时间
     */
    public void set(String key, String value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    /**
     * 获取字符串值
     */
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 删除键
     */
    public Boolean del(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 判断键是否存在
     */
    public Boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 设置过期时间
     */
    public Boolean expire(String key, long timeout, TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 获取剩余过期时间
     */
    public Long getExpire(String key, TimeUnit unit) {
        return redisTemplate.getExpire(key, unit);
    }

    // ======================= 数值操作 =======================

    /**
     * 自增（步长1）
     */
    public Long incr(String key) {
        return redisTemplate.opsForValue().increment(key);
    }

    /**
     * 自增（自定义步长）
     */
    public Long incrBy(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 自减（步长1）
     */
    public Long decr(String key) {
        return redisTemplate.opsForValue().decrement(key);
    }

    /**
     * 自减（自定义步长）
     */
    public Long decrBy(String key, long delta) {
        return redisTemplate.opsForValue().decrement(key, delta);
    }

    // ======================= 对象操作（JSON序列化） =======================

    /**
     * 存储对象为JSON字符串
     */
    public <T> void setObject(String key, T value) {
        try {
            String json = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Redis setObject 序列化失败: " + e.getMessage(), e);
        }
    }

    /**
     * 存储对象为JSON字符串（带过期时间）
     */
    public <T> void setObject(String key, T value, long timeout, TimeUnit unit) {
        try {
            String json = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, json, timeout, unit);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Redis setObject 序列化失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取对象（自动反序列化）
     */
    public <T> T getObject(String key, Class<T> clazz) {
        String json = redisTemplate.opsForValue().get(key);
        if (json == null) return null;
        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException("Redis getObject 反序列化失败: " + e.getMessage(), e);
        }
    }

    // ======================= 其他封装扩展 =======================

    /**
     * 返回底层模板（用于pipeline、hash、list操作）
     */
    public StringRedisTemplate getTemplate() {
        return redisTemplate;
    }
}
