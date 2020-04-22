package com.edu.cpp.cs.cs3560.io.tasks;


import com.edu.cpp.cs.cs3560.model.tasks.Task;
import com.edu.cpp.cs.cs3560.model.tasks.anti.AntiTask;
import com.edu.cpp.cs.cs3560.model.tasks.recurring.Frequency;
import com.edu.cpp.cs.cs3560.model.tasks.recurring.RecurringTask;
import com.edu.cpp.cs.cs3560.model.tasks.trans.TransientTask;
import com.edu.cpp.cs.cs3560.model.types.TaskTypes;
import com.edu.cpp.cs.cs3560.model.types.TaskTypes.AntiTasks;
import com.edu.cpp.cs.cs3560.model.types.TaskTypes.RecurringTasks;
import com.edu.cpp.cs.cs3560.model.types.TaskTypes.TransientTasks;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class TaskTypeDeserializer implements TaskDeserializer {
    private static final Gson gson = new Gson();



    @Override
    public List<Task> deserialize(String json) {
        return parseTasks(parseJson(json));
    }

    private List<Task> parseTasks(Collection<ParsedTask> parsed){
        return parsed.stream().map(this::parseTask).collect(Collectors.toList());
    }

    private Task parseTask(ParsedTask parsed){
        String name = parsed.name;
        String type = parsed.type;
        LocalDate date = parseDate(parsed.date);
        LocalTime startTime = parseTime(parsed.startTime);
        TemporalAmount duration = parseDuration(parsed.duration);

        Type ptype = TaskTypes.getTaskType(type);
        if(ptype == TransientTasks.class){
            return new TransientTask(name, type, date, startTime, duration);
        } else if (ptype == AntiTasks.class){
            return new AntiTask(name, type, date, startTime, duration);
        } else if(ptype == RecurringTasks.class){
            LocalDate endDate = parseDate(parsed.endDate);
            Frequency frequency = Frequency.getFrequency(parsed.frequency);

            return new RecurringTask(name, type, date, startTime, duration, endDate, frequency);
        } else {
            throw new IllegalArgumentException();
        }
    }

    private List<ParsedTask> parseJson(String json){
        List<ParsedTask> tasks = new ArrayList<>();

        JsonElement element = JsonParser.parseString(json);

        if(element.isJsonArray()){
            tasks.addAll(parseJsonArray(json));
        } else if(element.isJsonObject()){
            tasks.add(parseJsonObject(json));
        }


        return tasks;
    }


    private List<ParsedTask> parseJsonArray(String json){
        return gson.fromJson(json, new TypeToken<ArrayList<TaskParser.ParsedTask>>(){}.getType());
    }

    private ParsedTask parseJsonObject(String json){
        return gson.fromJson(json, ParsedTask.class);
    }

    private LocalDate parseDate(int date){
        return parseDate(String.valueOf(date));
    }

    private LocalDate parseDate(String date){
        return LocalDate.parse(date, DateTimeFormatter.BASIC_ISO_DATE);
    }

    private LocalTime parseTime(double time){
        int hour = (int) time;
        int minute = (int) ((time - hour) * 60);

        return LocalTime.of(hour, minute);
    }

    private TemporalAmount parseDuration(double duration){
        long hours = (long) duration;
        long minutes = (long) ((duration - hours) * 60);

        return Duration.ofHours(hours).plusMinutes(minutes);
    }




    public static class ParsedTask {

        @SerializedName("Name")
        private String name;

        @SerializedName("Type")
        private String type;

        @SerializedName(value = "Date", alternate = "StartDate")
        private int date;

        @SerializedName("StartTime")
        private double startTime;

        @SerializedName("Duration")
        private double duration;

        @SerializedName("EndDate")
        private int endDate;

        @SerializedName("Frequency")
        private int frequency;

        private ParsedTask(){}

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }


        public int getDate() {
            return date;
        }

        public void setDate(int date) {
            this.date = date;
        }

        public double getStartTime() {
            return startTime;
        }

        public void setStartTime(double startTime) {
            this.startTime = startTime;
        }

        public double getDuration() {
            return duration;
        }

        public void setDuration(double duration) {
            this.duration = duration;
        }

        public int getEndDate() {
            return endDate;
        }

        public void setEndDate(int endDate) {
            this.endDate = endDate;
        }

        public int getFrequency() {
            return frequency;
        }

        public void setFrequency(int frequency) {
            this.frequency = frequency;
        }

        @Override
        public String toString(){
            return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                    .append("Name", name)
                    .append("Type", type)
                    .append("Date", date)
                    .append("StartTime", startTime)
                    .append("Duration", duration)
                    .append("EndDate", endDate)
                    .append("Frequency", frequency)
                    .toString();
        }

    }

}
