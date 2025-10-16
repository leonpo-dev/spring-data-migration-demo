package com.datasync.util;

import com.datasync.config.SpringBeans;

import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 全局唯一 & 递增趋势 ID（Snowflake 风格），提供静态方法 getSEQ()
 * <p>
 * 结构(64-bit):
 * 0 | timestampDelta(41) | workerId(10) | sequence(12)
 */
public final class Sequence {

    // ====== 静态上下文（通过 AutoConfig 或懒加载赋值）======
    private static volatile RedisUtils REDIS;
    private static volatile int WORKER_ID;
    private static volatile boolean INITIALIZED = false;

    // ====== 常量配置 ======
    private static final long EPOCH_MS = Instant.parse("2024-01-01T00:00:00Z").toEpochMilli();
    private static final int WORKER_BITS = 10;                   // 0~1023
    private static final long WORKER_MASK = (1L << WORKER_BITS) - 1;
    private static final int SEQ_BITS = 12;                      // 0~4095
    private static final long SEQ_MASK = (1L << SEQ_BITS) - 1;

    private Sequence() {
    }

    /**
     * Spring 启动后由 GlobalSequenceAutoConfig 调用
     */
    public static synchronized void init(RedisUtils redisUtils, int workerId) {
        Objects.requireNonNull(redisUtils, "redisUtils must not be null");
        if (workerId < 0 || workerId > WORKER_MASK) {
            throw new IllegalArgumentException("workerId must be between 0 and " + WORKER_MASK);
        }
        if (!INITIALIZED) {
            REDIS = redisUtils;
            WORKER_ID = workerId;
            INITIALIZED = true;
        }
    }

    /**
     * 获取全局唯一ID（静态方法）
     */
    public static long getSEQ() {
        ensureInitialized();

        long now = System.currentTimeMillis();
        long tsDelta = now - EPOCH_MS;
        if (tsDelta < 0) {
            // 极端：本机时钟被拨回到纪元之前，等待到纪元
            tsDelta = waitUntil(EPOCH_MS);
        }

        // 毫秒 + workerId 维度做序列计数（原子自增）
        String key = "seq:" + now + ":" + WORKER_ID;
        Long seq = REDIS.incr(key);
        if (seq == null) throw new IllegalStateException("Redis INCR returned null");
        if (seq == 1L) {
            // 新Key设置短TTL，避免 key 爆炸
            REDIS.expire(key, 2, TimeUnit.SECONDS);
        }

        if (seq - 1 > SEQ_MASK) {
            // 当前毫秒生成超 4096 个，切到下一毫秒
            now = waitNextMillis(now);
            tsDelta = now - EPOCH_MS;
            key = "seq:" + now + ":" + WORKER_ID;
            seq = REDIS.incr(key);
            if (seq == null) throw new IllegalStateException("Redis INCR returned null");
            if (seq == 1L) REDIS.expire(key, 2, TimeUnit.SECONDS);
        }

        long seqVal = (seq - 1) & SEQ_MASK;
        return (tsDelta << (WORKER_BITS + SEQ_BITS)) | ((long) WORKER_ID << SEQ_BITS) | seqVal;
    }

    // ====== 初始化兜底（防止未触发 AutoConfig 的极端场景）======

    private static void ensureInitialized() {
        if (INITIALIZED) return;

        synchronized (Sequence.class) {
            if (INITIALIZED) return;

            // 兜底：从 Spring 容器拿 RedisUtils（要求 SpringBeans 已就绪）
            RedisUtils redis = SpringBeans.getBean(RedisUtils.class);

            // 兜底：workerId 从系统属性或环境变量读取（没有则 0）
            // -Did.worker-id=3  或  环境变量 ID_WORKER_ID=3
            int fallbackWorkerId = 0;
            String p = System.getProperty("id.worker-id");
            if (p == null || p.isEmpty()) {
                p = System.getenv("ID_WORKER_ID");
            }
            if (p != null && !p.isEmpty()) {
                try {
                    fallbackWorkerId = Integer.parseInt(p);
                } catch (NumberFormatException ignored) {
                }
            }

            init(redis, fallbackWorkerId);
        }
    }

    // ====== 辅助方法 ======
    private static long waitNextMillis(long lastTs) {
        long ts = System.currentTimeMillis();
        while (ts <= lastTs) {
            ts = System.currentTimeMillis();
        }
        return ts;
    }

    private static long waitUntil(long targetMs) {
        long ts = System.currentTimeMillis();
        while (ts < targetMs) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException ignored) {
            }
            ts = System.currentTimeMillis();
        }
        return ts - targetMs;
    }
}
