// =====================================================================================================================
// TransientTask.java
// =====================================================================================================================
/* About:
 *      This class handles the creation of transient tasks.
 *
 *      Implements the Task.java interface and inherits methods from NonRecurringTask.java.
 * */
// =====================================================================================================================
package com.edu.cpp.cs.cs3560.model.tasks.trans;

import com.edu.cpp.cs.cs3560.model.tasks.NonRecurringTask;
import com.edu.cpp.cs.cs3560.model.tasks.Task;
import com.edu.cpp.cs.cs3560.model.tasks.anti.AntiTask;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAmount;


public class TransientTask extends NonRecurringTask implements Task, Comparable<Task> {

    public TransientTask(String name, String type, LocalDate date, LocalTime startTime, TemporalAmount duration) {
        super(name, type, date, startTime, duration);
    }

    @Override
    public boolean overlap(final Task other){
        if(this.equals(other)) return false;

        if(other.getClass() == AntiTask.class) return false;

        return (this.getDateTime().isBefore(other.getEndDateTime()) && this.getEndDateTime().isAfter(other.getDateTime()));
    }

    @Override
    public boolean matchInterval(final Task other){
        if(this.equals(other)) return false;

        if(other.getClass() == AntiTask.class) return false;

        return this.getDateTime().equals(other.getDateTime()) && this.getDuration().equals(other.getDuration());
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) { return false; }
        if (obj == this) { return true; }
        if (obj.getClass() != getClass()) {
            return false;
        }

        final TransientTask other = (TransientTask) obj;
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
