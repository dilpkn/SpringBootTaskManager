package org.example.springtacksmanager;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import java.time.LocalDate;
import java.time.LocalDateTime;

record Task (

    @Null
    Long id,

    @NotNull
    Long creatorId,

    Long assignedUserId,

    StatusEnum statusEnum,

    LocalDateTime startDate,

    @NotNull
    @Future
    LocalDateTime endDate,

    @NotNull
    PriorityEnum priorityEnum,

    LocalDateTime doneDateTime

){}
