package com.edu.cpp.cs.cs3560.model.tasks.recurring;

import com.edu.cpp.cs.cs3560.model.tasks.AbstractTask;
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

public class RecurringTask extends AbstractTask implements Task {

    protected transient LocalDate startDate;
    protected transient LocalDate endDate;
    protected transient Frequency frequency;

    public RecurringTask(){}

    public RecurringTask(
            String name,
            String type,
            LocalDate date,
            LocalTime startTime,
            TemporalAmount duration
    ) {
        super(name, type, startTime, duration);
    }

    public RecurringTask(
            String name,
            String type,
            LocalDate date,
            LocalTime startTime,
            TemporalAmount duration,
            LocalDate endDate,
            Frequency frequency
    ) {
        this(name, type, date, date, startTime, duration, endDate, frequency);
    }

    public RecurringTask(
            String name,
            String type,
            LocalDate date,
            LocalDate startDate,
            LocalTime startTime,
            TemporalAmount duration,
            LocalDate endDate,
            Frequency frequency
    ) {
        this(name, type, date, startTime, duration);
        this.startDate = startDate;
        this.endDate = endDate;
        this.frequency = frequency;
    }

    public LocalDate getStartDate(){ return startDate; }

    public void setStartDate(LocalDate startDate){
        this.startDate = startDate;
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
        return prettyToString(ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE));
    }

    /*
    @Override
    public String toString(){
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("Name", name)
                .append("Type", type)
                .append("StartDate", parseDateToInteger(startDate))
                .append("StartTime", parseTimeToDouble(startTime))
                .append("Duration", duration)
                .append("EndDate", parseDateToInteger(endDate))
                .append("Frequency", frequency.getKey())
                .toString()
                .replace("{", "{\n")
                .replace("}", "\n}")
                .replace(",", ",\n");
    }
     */


    @Override
    public LocalDate getDate() {
        return startDate;
    }

    @Override
    public void setDate(LocalDate date) {
        this.startDate = date;
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
