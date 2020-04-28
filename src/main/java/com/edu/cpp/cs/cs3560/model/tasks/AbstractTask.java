package com.edu.cpp.cs.cs3560.model.tasks;


import com.google.gson.annotations.SerializedName;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import java.time.temporal.TemporalAmount;


public abstract class AbstractTask implements Task, Comparable<Task> {

    @SerializedName("Name")
    protected final String name;

    @SerializedName("Type")
    protected final String type;

    @SerializedName("StartTime")
    protected final LocalTime startTime;

    @SerializedName("Duration")
    protected final TemporalAmount duration;

    public AbstractTask(final String name, final String type, final LocalTime startTime, final TemporalAmount duration) {
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
    public String getType() {
        return type;
    }

    @Override
    public LocalTime getStartTime() {
        return startTime;
    }

    @Override
    public TemporalAmount getDuration() {
        return duration;
    }

    @Override
    public LocalTime getEndTime(){
        return startTime.plus(duration);
    }

    @Override
    public boolean overlap(final Task other){
        if(this.equals(other)) return false;

        return (this.getDateTime().isBefore(other.getEndDateTime()) && this.getEndDateTime().isAfter(other.getDateTime()));
    }

    @Override
    public boolean matchInterval(final Task other){
        if(this.equals(other)) return false;

        return this.getDateTime().equals(other.getDateTime()) && this.getDuration().equals(other.getDuration());
    }

    @Override
    public int compareTo(final Task other){
        final int cd = getDate().compareTo(other.getDate());
        if(cd == 0){
            final int ct = this.startTime.compareTo(other.getStartTime());
            if(ct == 0){
                return Duration.from(this.duration).compareTo(Duration.from(other.getDuration()));
            }

            return ct;
        }

        return cd;
    }

    protected final String prettyToString(final String s){
        return s.replace("{", "{\n")
                .replace("}", "\n}")
                .replace(",", ",\n");
    }

    protected final int parseDateToInteger(final LocalDate date){
        return Integer.parseInt(date.format(DateTimeFormatter.BASIC_ISO_DATE));
    }

    protected final double parseTimeToDouble(final LocalTime time){
        final double hour = time.getHour();
        final double minute = ((double) time.getMinute()) / 60.0;

        return hour + minute;
    }

    protected final double parseDuration(final TemporalAmount amount){
        final Duration duration = Duration.from(amount);
        final double hour = duration.toHoursPart();
        final double minute = ((double) duration.toMinutesPart()) / 60.0;


        return hour + minute;
    }

}
