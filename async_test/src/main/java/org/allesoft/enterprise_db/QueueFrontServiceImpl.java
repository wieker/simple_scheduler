package org.allesoft.enterprise_db;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.task.TaskExecutor;

public class QueueFrontServiceImpl implements QueueFrontService, ApplicationContextAware {
    private TaskExecutor taskExecutor;
    private ApplicationContext applicationContext;
    private Integer fillersCount;

    public QueueFrontServiceImpl(TaskExecutor taskExecutor, Integer fillersCount) {
        this.taskExecutor = taskExecutor;
        this.fillersCount = fillersCount;
    }

    @Override
    public void fill() {
        for (int i = 0; i < fillersCount; i ++) {
            taskExecutor.execute((Runnable) applicationContext.getBean("filler"));
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
