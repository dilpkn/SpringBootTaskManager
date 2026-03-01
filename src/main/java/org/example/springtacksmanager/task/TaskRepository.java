package org.example.springtacksmanager.task;


import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    List<TaskEntity> id(Long id);


    @Query("""
select t from TaskEntity t
where (:creatorId is null or t.creatorId = :creatorId)
and (:status is null or t.status = :status)
and (:priority is null or t.priority = :priority)
and (:assignedUserId is null or t.assignedUserId = :assignedUserId)
""")
    List<TaskEntity> searchByFilter(
            @Param("creatorId")Long creatorId,
            @Param("assignedUserId") Long assignedUserId,
            @Param("status") StatusEnum status,
            @Param("priority")PriorityEnum priority,
            Pageable pageable
    );
}
