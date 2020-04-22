package com.edu.cpp.cs.cs3560.model.tasks.recurring;

import com.edu.cpp.cs.cs3560.model.tasks.Task;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAmount;

public class RecurringTransientTask extends RecurringTask implements Task {
    private transient LocalDate date;

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
    public String toString(){
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("Name", name)
                .append("Type", type)
                .append("Date", parseDateToInteger(date))
                //.append("StartDate", parseDateToInteger(startDate))
                .append("StartTime", parseTimeToDouble(startTime))
                .append("Duration", parseDuration(duration))
                //.append("EndDate", parseDateToInteger(endDate))
                //.append("Frequency", frequency.getKey())
                .toString()
                .replace("{", "{\n")
                .replace("}", "\n}")
                .replace(",", ",\n");
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

}
