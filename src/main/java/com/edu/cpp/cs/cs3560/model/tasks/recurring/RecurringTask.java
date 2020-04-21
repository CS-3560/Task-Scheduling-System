package com.edu.cpp.cs.cs3560.model.tasks.recurring;

import com.edu.cpp.cs.cs3560.model.tasks.AbstractTask;
import com.edu.cpp.cs.cs3560.model.tasks.Task;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

public class RecurringTask extends AbstractTask implements Task {
    private LocalDate endDate;
    private Frequency frequency;

    public RecurringTask(){}

    public RecurringTask(
            String name,
            String type,
            LocalDate date,
            LocalTime startTime,
            Duration duration
    ) {
        super(name, type, date, startTime, duration);
    }

    public RecurringTask(
            String name,
            String type,
            LocalDate date,
            LocalTime startTime,
            Duration duration,
            LocalDate endDate,
            Frequency frequency
    ) {
        this(name, type, date, startTime, duration);
        this.endDate = endDate;
        this.frequency = frequency;
    }


    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Frequency getFrequency() {
        return frequency;
    }

    public void setFrequency(Frequency frequency) {
        this.frequency = frequency;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) { return false; }
        if (obj == this) { return true; }
        if (obj.getClass() != getClass()) {
            return false;
        }
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString(){
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("Name", name)
                .append("Type", type)
                .append("StartDate", date)
                .append("StartTime", startTime)
                .append("Duration", duration)
                .append("EndDate", endDate)
                .append("Frequency", frequency.getKey())
                .toString();
    }


}
