package org.example.springtacksmanager.task;

public record TaskFilter(
        Long creatorId,
        Long assignedUserId,
        StatusEnum status,
        PriorityEnum priority,
        Integer pageSize,
        Integer pageNumber
) {
}
