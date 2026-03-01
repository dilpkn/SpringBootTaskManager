package org.example.springtacksmanager.task;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class TaskService {

//    HashMap<Long, Task> tasksMap ;
//    private final AtomicLong idCounter;

    private final TaskRepository repository;

    private final TaskMapper mapper;

    public TaskService(TaskRepository repository, TaskMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public Task getTaskId(Long id) {

        TaskEntity entity = repository.findById(id).orElseThrow(() ->new EntityNotFoundException("Not found task by id = " + id));

        return mapper.toDomain(entity);
    }

    public List<Task> getTasks() {

        List<TaskEntity> allEntities = repository.findAll();

        List<Task> allTasks = allEntities.stream().map(mapper::toDomain).toList();
        return allTasks;
    }

    public Task createTask(Task task) {
        if(task.statusEnum()!=null){
            throw  new IllegalStateException("Task status should be empty");
        }

        if(!task.endDate().isAfter(task.startDate())){
            throw  new IllegalStateException("Task end date should be after task start date");
        }
        TaskEntity taskToCreate = mapper.toEntity(task);
        taskToCreate.setStatus(StatusEnum.CREATED);

        var savedEntity = repository.save(taskToCreate);
        return mapper.toDomain(savedEntity);
    }

    public Task updateTask(Long id, Task taskNew) {

        TaskEntity entity = repository.findById(id).orElseThrow(() ->new EntityNotFoundException("Not found task by id = " + id));

        if(!taskNew.endDate().isAfter(taskNew.startDate())){
            throw  new IllegalStateException("Task end date should be after task start date");
        }

        if(entity.getStatus() ==StatusEnum.IN_PROGRESS){
            throw  new IllegalStateException("Can't update task. It is already in progress");
        }

        if(entity.getStatus() ==StatusEnum.DONE){
            throw  new IllegalStateException("Can't update task. It is done");
        }

        TaskEntity taskToUpdate = mapper.toEntity(taskNew);
        taskToUpdate.setStatus(StatusEnum.CREATED);

        var updatedEntity = repository.save(taskToUpdate);
        return mapper.toDomain(updatedEntity);
    }

    public void deleteTask(Long id) {
        TaskEntity entity = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Not found task by id = " + id));

        if(entity.getStatus()!=StatusEnum.CREATED){
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

        if(entity.getStatus()!=StatusEnum.CREATED){
            throw  new IllegalStateException("Task status should be CREATED");
        }

        Long count = repository.findAll().stream()
                .filter(it->entity.getAssignedUserId().equals(it.getAssignedUserId()))
                .filter(it-> StatusEnum.IN_PROGRESS.equals(it.getStatus())).count();

        if(count>3){
            throw new IllegalArgumentException("User need to has not more than 5 tasks");
        }

        entity.setStatus(StatusEnum.IN_PROGRESS);

        var startedEntity = repository.save(entity);
        return mapper.toDomain(startedEntity);

    }

    public Task doneTask(Long id) {
        TaskEntity taskEntity = repository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Not found task by id = " + id));

        if(taskEntity.getStatus()!=StatusEnum.IN_PROGRESS){
            throw  new IllegalStateException("Task status should be in-progress");
        }

        taskEntity.setStatus(StatusEnum.DONE);
        taskEntity.setPriority(null);
        taskEntity.setDoneDateTime(LocalDateTime.now());

        var donedtask = repository.save(taskEntity);

        return mapper.toDomain(donedtask);

    }

    public List<Task> searchByFilter(
            TaskFilter filter
    ) {
        int pageSize = filter.pageSize()!=null?
                filter.pageSize() : 5;

        int pageNumber = filter.pageNumber()!=null ?
                filter.pageNumber() : 0;

        var pageable = Pageable.ofSize(pageSize).withPage(pageNumber);

        List<TaskEntity> entities = repository.searchByFilter(
                filter.creatorId(),
                filter.assignedUserId(),
                filter.status(),
                filter.priority(),
                pageable
        );

        List<Task> tasks = entities.stream().map(mapper::toDomain).toList();

        return tasks;
    }
}
