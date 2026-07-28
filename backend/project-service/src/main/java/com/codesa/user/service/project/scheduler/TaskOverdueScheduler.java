package com.codesa.user.service.project.scheduler;

import com.codesa.user.service.project.entity.TaskEntity;
import com.codesa.user.service.project.enums.TaskStatus;
import com.codesa.user.service.project.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class TaskOverdueScheduler {

    private static final Logger log = LoggerFactory.getLogger(TaskOverdueScheduler.class);

    private final TaskRepository taskRepository;

    public TaskOverdueScheduler(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Scheduled(cron = "${scheduler.overdue.cron}")
    public void markOverdueTasks() {
        List<TaskEntity> overdueTasks = taskRepository.findOverdueTasks(LocalDateTime.now());

        for (TaskEntity task : overdueTasks) {
            task.setStatus(TaskStatus.OVERDUE.name());
        }
        if (!overdueTasks.isEmpty()) {
            taskRepository.saveAll(overdueTasks);
            log.info("Updated {} task(s) to OVERDUE", overdueTasks.size());
        }else{
            log.info("No se encontraron tareas para cambio de estado");

        }
    }
}
