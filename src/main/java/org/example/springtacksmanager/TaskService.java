package org.example.springtacksmanager;

import jakarta.persistence.EntityNotFoundException;
import org.jspecify.annotations.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TaskService {

//    HashMap<Long, Task> tasksMap ;
//    private final AtomicLong idCounter;

    private  final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public Task getTaskId(Long id) {

        TaskEntity entity = repository.findById(id).orElseThrow(() ->new EntityNotFoundException("Not found task by id = " + id));

        return toDomainTask(entity);
    }

    private Task toDomainTask(TaskEntity entity){
        return new Task(
                entity.getId(),
                entity.getCreatorId(),
                entity.getAssignedUserId(),
                entity.getStatusEnum(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getPriorityEnum(),
                entity.getDoneDateTime()
        );
    }

    public List<Task> getTasks() {

        List<TaskEntity> allEntities = repository.findAll();

        List<Task> allTasks = allEntities.stream().map(this::toDomainTask).toList();
        return allTasks;
    }

    public Task createTask(Task task) {
        if(task.statusEnum()!=null){
            throw  new IllegalStateException("Task status should be empty");
        }

        if(!task.endDate().isAfter(task.startDate())){
            throw  new IllegalStateException("Task end date should be after task start date");
        }

        TaskEntity taskToCreate = new TaskEntity(
                null,
                task.creatorId(),
                task.assignedUserId(),
                StatusEnum.CREATED,
                task.startDate(),
                task.endDate(),
                task.priorityEnum(),
                task.doneDateTime()
        );

        repository.save(taskToCreate);
        return toDomainTask(taskToCreate);
    }

    public Task updateTask(Long id, Task taskNew) {

        TaskEntity entity = repository.findById(id).orElseThrow(() ->new EntityNotFoundException("Not found task by id = " + id));

        if(!taskNew.endDate().isAfter(taskNew.startDate())){
            throw  new IllegalStateException("Task end date should be after task start date");
        }

        if(entity.getStatusEnum() ==StatusEnum.IN_PROGRESS){
            throw  new IllegalStateException("Can't update task. It is already in progress");
        }

        if(entity.getStatusEnum() ==StatusEnum.DONE){
            throw  new IllegalStateException("Can't update task. It is done");
        }

        TaskEntity taskToUpdate = new TaskEntity(
                id,
                taskNew.creatorId(),
                taskNew.assignedUserId(),
                StatusEnum.CREATED,
                taskNew.startDate(),
                taskNew.endDate(),
                taskNew.priorityEnum(),
                taskNew.doneDateTime()
        );
        repository.save(taskToUpdate);
        return toDomainTask(taskToUpdate);
    }

    public void deleteTask(Long id) {
        TaskEntity entity = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Not found task by id = " + id));

        if(entity.getStatusEnum()!=StatusEnum.CREATED){
            throw  new IllegalStateException("Task status should be CREATED");
        }
        repository.deleteById(id);
    }

    public Task startTask(Long id) {
        TaskEntity entity = repository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Not found task by id = " + id));

        if(entity.getAssignedUserId()==null){
            throw  new IllegalStateException("Task assigned user is null");
        }

        if(entity.getStatusEnum()!=StatusEnum.CREATED){
            throw  new IllegalStateException("Task status should be CREATED");
        }

        Long count = repository.findAll().stream()
                .filter(it->entity.getAssignedUserId().equals(it.getAssignedUserId()))
                .filter(it-> StatusEnum.IN_PROGRESS.equals(it.getStatusEnum())).count();

        if(count>3){
            throw new IllegalArgumentException("User need to has not more than 5 tasks");
        }

        TaskEntity taskToUpdate = new TaskEntity(
                entity.getId(),
                entity.getCreatorId(),
                entity.getAssignedUserId(),
                StatusEnum.IN_PROGRESS,
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getPriorityEnum(),
                entity.getDoneDateTime()
        );

        repository.save(taskToUpdate);
        return toDomainTask(taskToUpdate);

    }

    public Task doneTask(Long id) {
        TaskEntity taskEntity = repository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Not found task by id = " + id));

        if(taskEntity.getStatusEnum()!=StatusEnum.IN_PROGRESS){
            throw  new IllegalStateException("Task status should be in-progress");
        }

        var taskToDone = new TaskEntity(
                id,
                taskEntity.getCreatorId(),
                taskEntity.getAssignedUserId(),
                StatusEnum.DONE,
                taskEntity.getStartDate(),
                taskEntity.getEndDate(),
                null,
                LocalDateTime.now()
        );

        repository.save(taskToDone);

        return toDomainTask(taskToDone);

    }
}
