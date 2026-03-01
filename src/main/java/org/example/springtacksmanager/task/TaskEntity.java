package org.example.springtacksmanager.task;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "creator_id")
    private Long creatorId;

    @Column(name = "assigned_user_id")
    private Long assignedUserId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusEnum status;

    @Column(name = "start_date")
    private  LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private PriorityEnum priority;

    @Column(name = "done_date_time")
    private LocalDateTime doneDateTime;

    public TaskEntity(Long id, Long creatorId, Long assignedUserId, StatusEnum status,
                      LocalDateTime startDate, LocalDateTime endDate,
                      PriorityEnum priority, LocalDateTime doneDateTime) {
        this.id = id;
        this.creatorId = creatorId;
        this.assignedUserId = assignedUserId;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.priority = priority;
        this.doneDateTime = doneDateTime;
    }

    public LocalDateTime getDoneDateTime() {
        return doneDateTime;
    }

    public void setDoneDateTime(LocalDateTime doneDateTime) {
        this.doneDateTime = doneDateTime;
    }

    public TaskEntity() {

    }

    public Long getId() {
        return id;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public Long getAssignedUserId() {
        return assignedUserId;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }




    public PriorityEnum getPriority() {
        return priority;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public void setAssignedUserId(Long assignedUserId) {
        this.assignedUserId = assignedUserId;
    }

    public void setStatus(StatusEnum statusEnum) {
        this.status = statusEnum;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public void setPriority(PriorityEnum priorityEnum) {
        this.priority = priorityEnum;
    }
}
