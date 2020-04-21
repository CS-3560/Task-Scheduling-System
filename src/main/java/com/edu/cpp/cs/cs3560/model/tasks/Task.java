package com.edu.cpp.cs.cs3560.model.tasks;

import com.edu.cpp.cs.cs3560.model.Mappable;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

public interface Task extends Mappable {

    String getName();

    void setName(String name);

    String getType();

    void setType(String type);

    LocalDate getDate();

    void setDate(LocalDate date);

    LocalTime getStartTime();

    void setStartTime(LocalTime startTime);

    Duration getDuration();

    void setDuration(Duration duration);

    LocalTime getEndTime();

}
