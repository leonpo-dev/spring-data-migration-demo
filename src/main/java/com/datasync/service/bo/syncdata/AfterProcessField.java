package com.datasync.service.bo.syncdata;

import java.util.function.Function;

/**
 * @author Leonpo
 * @date 2023/5/15 18:13
 */
public class AfterProcessField<E> {

    private E target;

    /**
     * 后置处理服务
     */
    private Function<E, Boolean> afterProcessUpdateService;


    public AfterProcessField(E target) {
        this.target = target;
    }

    public E getTarget() {
        return target;
    }


    public Function<E, Boolean> getAfterProcessUpdateService() {
        return afterProcessUpdateService;
    }

    public void setAfterProcessUpdateService(Function<E, Boolean> afterProcessUpdateService) {
        this.afterProcessUpdateService = afterProcessUpdateService;
    }
}
