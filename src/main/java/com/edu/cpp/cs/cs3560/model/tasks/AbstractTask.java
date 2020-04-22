package com.edu.cpp.cs.cs3560.model.tasks;


import com.google.gson.annotations.SerializedName;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import java.time.temporal.TemporalAmount;


public abstract class AbstractTask implements Task, Comparable<Task> {

    @SerializedName("Name")
    protected String name;

    @SerializedName("Type")
    protected String type;

    @SerializedName("StartTime")
    protected LocalTime startTime;

    @SerializedName("Duration")
    protected TemporalAmount duration;

    public AbstractTask(){}

    public AbstractTask(String name, String type, LocalTime startTime, TemporalAmount duration) {
        this.name = name;
        this.type = type;
        this.startTime = startTime;
        this.duration = duration;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public LocalTime getStartTime() {
        return startTime;
    }

    @Override
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public TemporalAmount getDuration() {
        return duration;
    }

    @Override
    public void setDuration(TemporalAmount duration) {
        this.duration = duration;
    }

    @Override
    public LocalTime getEndTime(){
        return startTime.plus(duration);
    }

    @Override
    public int compareTo(Task other){
        int cd = getDate().compareTo(other.getDate());
        if(cd == 0){
            int ct = this.startTime.compareTo(other.getStartTime());
            if(ct == 0){
                return Duration.from(this.duration).compareTo(Duration.from(other.getDuration()));
            }

            return ct;
        }

        return cd;
    }

    protected final int parseDateToInteger(LocalDate date){
        return Integer.parseInt(date.format(DateTimeFormatter.BASIC_ISO_DATE));
    }

    protected final double parseTimeToDouble(LocalTime time){
        double hour = time.getHour();
        double minute = ((double) time.getMinute()) / 60.0;

        return hour + minute;
    }

    protected final double parseDuration(TemporalAmount amount){
        Duration duration = Duration.from(amount);
        double hour = duration.toHoursPart();
        double minute = ((double) duration.toMinutesPart()) / 60.0;


        return hour + minute;
    }

}
