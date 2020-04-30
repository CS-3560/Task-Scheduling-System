// =====================================================================================================================
// RecurringTask.java
// =====================================================================================================================
/* About:
 *      This class handles the creation of Recurring Tasks.
 *
 *      Implements the Task.java interface and inherits methods from AbstractTask.java
 * */
// =====================================================================================================================
package com.edu.cpp.cs.cs3560.model.tasks.recurring;

import com.edu.cpp.cs.cs3560.model.tasks.AbstractTask;
import com.edu.cpp.cs.cs3560.model.tasks.Task;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAmount;
import java.util.LinkedList;
import java.util.List;

public class RecurringTask extends AbstractTask implements Task, Comparable<Task> {
    protected transient final LocalDate startDate;
    protected transient final LocalDate endDate;
    protected transient final Frequency frequency;

    public RecurringTask(
            final String name,
            final String type,
            final LocalDate date,
            final LocalTime startTime,
            final TemporalAmount duration,
            final LocalDate endDate,
            final Frequency frequency
    ) {
        super(name, type, startTime, duration);
        this.startDate = date;
        this.endDate = endDate;
        this.frequency = frequency;
    }

    public LocalDate getStartDate(){ return startDate; }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Frequency getFrequency() {
        return frequency;
    }

    @Override
    public boolean overlap(final Task other){
        if(this.equals(other)) return false;

        if(other.getClass() == RecurringTransientTask.class) return false;

        for(final LocalDateTime rdate : getDates()){
            if(rdate.isBefore(other.getEndDateTime()) && rdate.plus(duration).isAfter(other.getDateTime())){
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean matchInterval(final Task other){
        if(this.equals(other)) return false;

        if(other.getClass() == RecurringTransientTask.class) return false;

        for(final LocalDateTime rdate : getDates()){
            if(rdate.equals(other.getDateTime()) && this.getDuration().equals(other.getDuration())){
                return true;
            }
        }

        return false;
    }

    public List<LocalDateTime> getDates(){
        final List<LocalDateTime> dates = new LinkedList<>();

        LocalDateTime current = LocalDateTime.of(startDate, startTime);
        while(current.isBefore(getEndDateTime())){
            dates.add(current);
            current = current.plus(1, frequency.getUnit());
        }

        return dates;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) { return false; }
        if (obj == this) { return true; }
        if (obj.getClass() != getClass()) {
            return false;
        }

        final RecurringTask other = (RecurringTask) obj;
        return new EqualsBuilder()
                .append(name, other.name)
                .append(type, other.type)
                .append(startDate, other.startDate)
                .append(startTime, other.startTime)
                .append(duration, other.duration)
                .append(endDate, other.endDate)
                .append(frequency, other.frequency)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(name)
                .append(type)
                .append(startDate)
                .append(startTime)
                .append(duration)
                .append(endDate)
                .append(frequency)
                .toHashCode();
    }


    @Override
    public String toString(){
        return prettyToString(
                new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                        .append("Name", name)
                        .append("Type", type)
                        .append("StartDate", parseDateToInteger(startDate))
                        .append("StartTime", parseTimeToDouble(startTime))
                        .append("Duration", parseDuration(duration))
                        .append("EndDate", parseDateToInteger(endDate))
                        .append("Frequency", frequency.getKey())
                        .toString()
        );
    }



    @Override
    public LocalDate getDate() {
        return startDate;
    }

    @Override
    public LocalDateTime getDateTime(){
        return LocalDateTime.of(getStartDate(), getStartTime());
    }

    @Override
    public LocalDateTime getEndDateTime(){
        return LocalDateTime.of(getEndDate(), getEndTime());
    }

}
