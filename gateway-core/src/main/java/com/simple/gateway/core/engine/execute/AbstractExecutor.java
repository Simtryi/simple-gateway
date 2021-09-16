package com.simple.gateway.core.engine.execute;

/**
 * Executor 的抽象类
 */
public abstract class AbstractExecutor implements Executor {

    @Override
    public void execute(Object... args) {
        doExecute(args);
    }

    /**
     * 执行具体的任务
     */
    protected abstract void doExecute(Object... args);

}
