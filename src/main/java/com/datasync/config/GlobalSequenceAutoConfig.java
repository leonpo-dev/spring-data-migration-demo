package com.datasync.config;


import com.datasync.util.RedisUtils;
import com.datasync.util.Sequence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * 在 Spring 启动完成后，将 RedisUtils 与 workerId 注入到 GlobalSequence 的静态上下文中
 */
@Configuration
public class GlobalSequenceAutoConfig {

    private final RedisUtils redisUtils;

    /**
     * 建议在 application.yml 配置： id.worker-id: 1~1023
     */
    @Value("${id.worker-id:0}")
    private int workerId;

    public GlobalSequenceAutoConfig(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }

    @PostConstruct
    public void init() {
        Sequence.init(redisUtils, workerId);
    }
}
