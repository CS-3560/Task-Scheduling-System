// =====================================================================================================================
// RecurringTransientTask.java
// =====================================================================================================================
/* About:
 *      This class handles the creation of recurring task instances.
 *
 *      An extension of RecurringTask.java.
 * */
// =====================================================================================================================
package com.edu.cpp.cs.cs3560.model.tasks.recurring;

import com.edu.cpp.cs.cs3560.model.tasks.Task;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAmount;


public class RecurringTransientTask extends RecurringTask implements Task, Comparable<Task>, Serializable {
    private static final long serialVersionUID = -678024661241951079L;

    @SerializedName("Date")
    private final LocalDate date;

    public RecurringTransientTask(
            final String name,
            final String type,
            final LocalDate date,
            final LocalDate startDate,
            final LocalTime startTime,
            final TemporalAmount duration,
            final LocalDate endDate,
            final Frequency frequency
    ){
        super(name, type, startDate, startTime, duration, endDate, frequency);
        this.date = date;
    }

    @Override
    public LocalDate getDate(){ return date; }

    @Override
    public LocalDateTime getDateTime(){
        return LocalDateTime.of(date, startTime);
    }

    @Override
    public LocalDateTime getEndDateTime(){
        return LocalDateTime.of(date, getEndTime());
    }

    @Override
    public LocalDate getEndDate(){
        return date;
    }

    @Override
    public boolean overlap(final Task other){
        if(this.equals(other)) return false;

        if(other.getClass() == RecurringTask.class) return false;

        return (this.getDateTime().isBefore(other.getEndDateTime()) && this.getEndDateTime().isAfter(other.getDateTime()));
    }

    @Override
    public boolean matchInterval(final Task other){
        if(this.equals(other)) return false;

        if(other.getClass() == RecurringTask.class) return false;

        return this.getDate().equals(other.getDate()) && this.getDuration().equals(other.getDuration());
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) { return false; }
        if (obj == this) { return true; }
        if (obj.getClass() != getClass()) {
            return false;
        }

        final RecurringTransientTask other = (RecurringTransientTask) obj;
        return new EqualsBuilder()
                .append(name, other.name)
                .append(type, other.type)
                .append(date, other.date)
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
                .append(date)
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
                        .append("Date", parseDateToInteger(date))
                        .append("StartTime", parseTimeToDouble(startTime))
                        .append("Duration", parseDuration(duration))
                        .toString()
        );
    }

}
