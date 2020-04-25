package com.edu.cpp.cs.cs3560.model.tasks.recurring;

import com.edu.cpp.cs.cs3560.model.tasks.Task;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAmount;

public class RecurringTransientTask extends RecurringTask implements Task {

    @SerializedName("Date")
    private LocalDate date;

    public RecurringTransientTask(
            String name,
            String type,
            LocalDate date,
            LocalDate startDate,
            LocalTime startTime,
            TemporalAmount duration,
            LocalDate endDate,
            Frequency frequency
    ){
        super(name, type, date, startDate, startTime, duration, endDate, frequency);
        this.date = date;
    }

    @Override
    public LocalDate getDate(){ return date; }

    @Override
    public void setDate(LocalDate date){
        this.date = date;
    }

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
    public boolean equals(Object obj) {
        if (obj == null) { return false; }
        if (obj == this) { return true; }
        if (obj.getClass() != getClass()) {
            return false;
        }

        RecurringTransientTask other = (RecurringTransientTask) obj;
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
