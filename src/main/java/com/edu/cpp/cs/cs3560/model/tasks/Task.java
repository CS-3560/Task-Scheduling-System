// =====================================================================================================================
// Task.java
// =====================================================================================================================
/* About:
 *      Interface for task classes.
 * */
// =====================================================================================================================
package com.edu.cpp.cs.cs3560.model.tasks;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAmount;

public interface Task extends Comparable<Task>{

    String getName();

    String getType();

    LocalDate getDate();

    LocalTime getStartTime();

    TemporalAmount getDuration();

    LocalTime getEndTime();

    LocalDateTime getDateTime();

    LocalDateTime getEndDateTime();

    boolean overlap(Task other);

    boolean matchInterval(Task other);

}
