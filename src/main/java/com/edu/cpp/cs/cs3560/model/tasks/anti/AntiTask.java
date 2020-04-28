package com.edu.cpp.cs.cs3560.model.tasks.anti;

import com.edu.cpp.cs.cs3560.model.tasks.NonRecurringTask;
import com.edu.cpp.cs.cs3560.model.tasks.Task;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAmount;


public class AntiTask extends NonRecurringTask implements Task, Comparable<Task> {

    public AntiTask(final String name, final String type, final LocalDate date, final LocalTime startTime, final TemporalAmount duration) {
        super(name, type, date, startTime, duration);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) { return false; }
        if (obj == this) { return true; }
        if (obj.getClass() != getClass()) {
            return false;
        }

        final AntiTask other = (AntiTask) obj;
        return new EqualsBuilder()
                .append(name, other.name)
                .append(type, other.type)
                .append(date, other.date)
                .append(startTime, other.startTime)
                .append(duration, other.duration)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(name)
                .append(type)
                .append(date)
                .append(startTime)
                .append(duration)
                .toHashCode();
    }

    @Override
    public String toString(){
        return prettyToString(
                new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                        .append("Name", name)
                        .append("Type", type)
                        .append("Date", parseDateToInteger(date))
                        .append("StartTime", parseTimeToDouble(startTime))
                        .append("Duration", duration)
                        .toString()
        );
    }

}
