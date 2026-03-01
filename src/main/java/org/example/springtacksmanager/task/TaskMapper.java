package org.example.springtacksmanager.task;

import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public Task toDomain(TaskEntity entity){
        return new Task(
                entity.getId(),
                entity.getCreatorId(),
                entity.getAssignedUserId(),
                entity.getStatus(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getPriority(),
                entity.getDoneDateTime()
        );
    }


    public TaskEntity toEntity(Task task){
        return new TaskEntity(
                task.id(),
                task.creatorId(),
                task.assignedUserId(),
                task.statusEnum(),
                task.startDate(),
                task.endDate(),
                task.priorityEnum(),
                task.doneDateTime()
        );
    }

}
