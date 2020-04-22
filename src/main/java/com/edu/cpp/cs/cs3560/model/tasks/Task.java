package com.edu.cpp.cs.cs3560.model.tasks;

import com.edu.cpp.cs.cs3560.model.Mappable;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAmount;

public interface Task extends Comparable<Task>{

    String getName();

    void setName(String name);

    String getType();

    void setType(String type);

    LocalDate getDate();

    void setDate(LocalDate date);

    LocalTime getStartTime();

    void setStartTime(LocalTime startTime);

    TemporalAmount getDuration();

    void setDuration(TemporalAmount duration);

    LocalTime getEndTime();

    LocalDateTime getDateTime();

    LocalDateTime getEndDateTime();

}
