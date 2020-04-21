package com.edu.cpp.cs.cs3560.model.tasks;


import com.edu.cpp.cs.cs3560.model.Mappable;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class AbstractTask implements Task, Comparable<Task>, Mappable {
    protected String name;
    protected String type;
    protected LocalDate date;
    protected LocalTime startTime;
    protected Duration duration;

    public AbstractTask(){}

    public AbstractTask(String name, String type, LocalDate date, LocalTime startTime, Duration duration) {
        this.name = name;
        this.type = type;
        this.date = date;
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
    public LocalDate getDate() {
        return date;
    }

    @Override
    public void setDate(LocalDate date) {
        this.date = date;
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
    public Duration getDuration() {
        return duration;
    }

    @Override
    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalTime getEndTime(){
        return startTime.plus(duration);
    }

    @Override
    public Map<String, ?> toMap(){
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("Name", name);
        map.put("Type", type);
        map.put("Date", date);
        map.put("StartTime", startTime);
        map.put("Duration", duration);

        return map;
    }

    @Override
    public int compareTo(Task other){
        int cd = this.date.compareTo(other.getDate());
        if(cd == 0){
            int ct = this.startTime.compareTo(other.getStartTime());
            if(ct == 0){
                return this.duration.compareTo(other.getDuration());
            }

            return ct;
        }

        return cd;
    }

}
