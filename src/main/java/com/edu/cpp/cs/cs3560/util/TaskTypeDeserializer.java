// =====================================================================================================================
// TaskTypeDeserializer.java
// =====================================================================================================================
/* About:
 *      This class is designed to take a stream of bits and convert them into an object for use throughout the code.
 *      Used when the user wishes to read a schedule from a file.
 * */
// =====================================================================================================================
package com.edu.cpp.cs.cs3560.util;

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
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class TaskTypeDeserializer implements TaskDeserializer {
    private static final Gson gson = new Gson();


    @Override
    public List<Task> deserialize(final String json) {
        return parseTasks(parseJson(json));
    }

    private List<Task> parseTasks(final Collection<DeserializedTask> parsed){
        return parsed.stream().map(this::parseTask).collect(Collectors.toList());
    }

    private Task parseTask(final DeserializedTask parsed){
        final String name = parsed.name;
        final String type = parsed.type;
        final LocalDate date = TaskParser.parseDate(parsed.date);
        final LocalTime startTime = TaskParser.parseTime(parsed.startTime);
        final TemporalAmount duration = TaskParser.parseDuration(parsed.duration);

        final Type ptype = TaskTypes.getTaskType(type);
        if(ptype == TransientTasks.class){
            return new TransientTask(name, type, date, startTime, duration);
        } else if (ptype == AntiTasks.class){
            return new AntiTask(name, type, date, startTime, duration);
        } else if(ptype == RecurringTasks.class){
            final LocalDate endDate = TaskParser.parseDate(parsed.endDate);
            final Frequency frequency = Frequency.getFrequency(parsed.frequency);

            return new RecurringTask(name, type, date, startTime, duration, endDate, frequency);
        } else {
            throw new IllegalArgumentException();
        }
    }

    private List<DeserializedTask> parseJson(final String json){
        final List<DeserializedTask> tasks = new ArrayList<>();

        final JsonElement element = JsonParser.parseString(json);
        if(element.isJsonArray()){
            tasks.addAll(parseJsonArray(json));
        } else if(element.isJsonObject()){
            tasks.add(parseJsonObject(json));
        }

        return tasks;
    }


    private List<DeserializedTask> parseJsonArray(final String json){
        return gson.fromJson(json, new TypeToken<ArrayList<DeserializedTask>>(){}.getType());
    }

    private DeserializedTask parseJsonObject(final String json){
        return gson.fromJson(json, DeserializedTask.class);
    }

    private static final class DeserializedTask {

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


        private DeserializedTask(){}

        public String getName() {
            return name;
        }

        public void setName(final String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(final String type) {
            this.type = type;
        }

        public int getDate() {
            return date;
        }

        public void setDate(final int date) {
            this.date = date;
        }

        public double getStartTime() {
            return startTime;
        }

        public void setStartTime(final double startTime) {
            this.startTime = startTime;
        }

        public double getDuration() {
            return duration;
        }

        public void setDuration(final double duration) {
            this.duration = duration;
        }

        public int getEndDate() {
            return endDate;
        }

        public void setEndDate(final int endDate) {
            this.endDate = endDate;
        }

        public int getFrequency() {
            return frequency;
        }

        public void setFrequency(final int frequency) {
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
