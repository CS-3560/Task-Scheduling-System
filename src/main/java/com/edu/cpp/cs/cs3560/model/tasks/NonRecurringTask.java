package com.edu.cpp.cs.cs3560.model.tasks;

import com.google.gson.annotations.SerializedName;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAmount;

public abstract class NonRecurringTask extends AbstractTask implements Task {

    @SerializedName("Date")
    protected LocalDate date;

    public NonRecurringTask(String name, String type, LocalDate date, LocalTime startTime, TemporalAmount duration) {
        super(name, type, startTime, duration);
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
        return LocalDateTime.of(getDate(), getStartTime());
    }

    @Override
    public LocalDateTime getEndDateTime(){
        return LocalDateTime.of(getDate(), getEndTime());
    }

}
