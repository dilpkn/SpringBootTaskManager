package org.example.springtacksmanager.task;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    TaskService taskService;

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping()
    public List<Task> getTasks() {
        logger.info("Get tasks");
        return taskService.getTasks();
    }

    @GetMapping("/{id}")
    public Task getTaskId(@PathVariable Long id) {
        logger.info("Get task with id = " + id);
        return taskService.getTaskId(id);
    }



    @PostMapping()
    public ResponseEntity<Task> createTask(
            @Valid @RequestBody Task task) {
        logger.info("task was created");
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(task));
    }

    @PutMapping("/{id}")
    public ResponseEntity updateTask(@PathVariable Long id, @Valid @RequestBody Task task) {
        logger.info("task was updated");
        return ResponseEntity.ok(taskService.updateTask(id, task));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        logger.info("task was deleted");
        taskService.deleteTask(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<Task> startTask(@PathVariable Long id) {
        logger.info("task was started");
        Task task;
            task = taskService.startTask(id);
        return ResponseEntity.ok(task);
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<Task> doneTask(@PathVariable Long id) {
        logger.info("task was done");
        return ResponseEntity.ok(taskService.doneTask(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Task>> searchTask(
            @RequestParam(name = "creatorId", required = false) Long creatorId,
            @RequestParam(name = "assignedUserId", required = false) Long assignedUserId,
            @RequestParam(name = "status", required = false) StatusEnum status,
            @RequestParam(name = "priority", required = false) PriorityEnum priority,
            @RequestParam(name = "pageSize", required = false) Integer pageSize,
            @RequestParam(name = "pageNumber", required = false) Integer pageNumber
    ) {
        logger.info("search task");

        TaskFilter filter = new TaskFilter(
                creatorId, assignedUserId,status,priority,pageSize,pageNumber
        );

        return ResponseEntity.ok(taskService.searchByFilter(filter));
    }

}
