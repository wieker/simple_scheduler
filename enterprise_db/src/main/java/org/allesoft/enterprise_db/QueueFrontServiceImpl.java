package org.allesoft.enterprise_db;

import org.springframework.core.task.TaskExecutor;

import java.util.List;

public class QueueFrontServiceImpl implements QueueFrontService {
    private List<Runnable> fillers;
    private TaskExecutor taskExecutor;

    public QueueFrontServiceImpl(List<Runnable> fillers, TaskExecutor taskExecutor) {
        this.fillers = fillers;
        this.taskExecutor = taskExecutor;
    }

    @Override
    public void fill() {
        fillers.forEach(taskExecutor::execute);
    }
}
