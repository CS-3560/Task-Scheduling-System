// =====================================================================================================================
// TaskTypeSerializer.java
// =====================================================================================================================
/* About:
*       This class is designed to take an object and convert it into a stream of bits for file IO.
*       Used in storing our tasks and schedules to the appropriate JSON file format.
* */
// =====================================================================================================================
package com.edu.cpp.cs.cs3560.util;

import com.edu.cpp.cs.cs3560.model.tasks.Task;
import com.edu.cpp.cs.cs3560.model.tasks.anti.AntiTask;
import com.edu.cpp.cs.cs3560.model.tasks.recurring.Frequency;
import com.edu.cpp.cs.cs3560.model.tasks.recurring.RecurringTask;
import com.edu.cpp.cs.cs3560.model.tasks.recurring.RecurringTransientTask;
import com.edu.cpp.cs.cs3560.model.tasks.trans.TransientTask;
import com.edu.cpp.cs.cs3560.model.types.TaskTypes;
import com.edu.cpp.cs.cs3560.model.types.TaskTypes.AntiTasks;
import com.edu.cpp.cs.cs3560.model.types.TaskTypes.RecurringTasks;
import com.edu.cpp.cs.cs3560.model.types.TaskTypes.TransientTasks;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.apache.commons.collections.CollectionUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAmount;
import java.util.Collection;
import java.util.Set;

public class TaskTypeSerializer implements TaskSerializer {
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
            .addDeserializationExclusionStrategy(TaskExclusionStrategy.get())
            .registerTypeAdapter(TransientTask.class, TaskTypeAdapter.get())
            .registerTypeAdapter(AntiTask.class, TaskTypeAdapter.get())
            .registerTypeAdapter(RecurringTask.class, RecurringTaskTypeAdapter.get())
            .registerTypeAdapter(RecurringTransientTask.class, TaskTypeAdapter.get())
            .create();

    @Override
    public String serialize(final Task task) {
        return gson.toJson(task);
    }

    @Override
    public String serialize(final Collection<Task> tasks) {
        return gson.toJson(tasks);
    }

    private static class TaskTypeAdapter extends TypeAdapter<Task> {
        private static final TaskTypeAdapter adapter = new TaskTypeAdapter();

        public static TypeAdapter<? extends Task> get(){
            return adapter;
        }

        @Override
        public void write(final JsonWriter out, final Task value) throws IOException {
            out.beginObject();
            out.name("Name").value(value.getName());
            out.name("Type").value(value.getType());
            out.name("Date").value(TaskParser.parseDateToInteger(value.getDate()));
            out.name("StartTime").value(TaskParser.parseTimeToDouble(value.getStartTime()));
            out.name("Duration").value(TaskParser.parseDuration(value.getDuration()));
            out.endObject();
        }

        @Override
        public Task read(final JsonReader in) throws IOException {
            final FileTask rtask = new FileTask();

            in.beginObject();

            String field = null;
            while(in.hasNext()){
                JsonToken token = in.peek();
                if(token.equals(JsonToken.NAME)){
                    field = in.nextName();
                }

                if("Name".equals(field)){
                    token = in.peek();
                    rtask.name = in.nextString();
                }

                if("Type".equals(field)){
                    token = in.peek();
                    rtask.type = in.nextString();
                }

                if("Date".equals(field) || "StartDate".equals(field)){
                    token = in.peek();
                    rtask.date = in.nextInt();
                }

                if("StartTime".equals(field)){
                    token = in.peek();
                    rtask.startTime = in.nextDouble();
                }

                if("Duration".equals(field)){
                    token = in.peek();
                    rtask.duration = in.nextDouble();
                }

                if("EndDate".equals(field)){
                    token = in.peek();
                    rtask.endDate = in.nextInt();
                }

                if("Frequency".equals(field)){
                    token = in.peek();
                    rtask.frequency = in.nextInt();
                }
            }
            in.endObject();

            final String name = rtask.name;
            final String type = rtask.type;
            final LocalDate date = TaskParser.parseDate(rtask.date);
            final LocalTime startTime = TaskParser.parseTime(rtask.startTime);
            final TemporalAmount duration = TaskParser.parseDuration(rtask.duration);

            final Type rtype = TaskTypes.getTaskType(type);
            if(rtype == TransientTasks.class){
                return new TransientTask(name, type, date, startTime, duration);
            } else if(rtype == AntiTasks.class){
                return new AntiTask(name, type, date, startTime, duration);
            } else if(rtype == RecurringTasks.class){
                final LocalDate endDate = TaskParser.parseDate(rtask.endDate);
                final Frequency frequency = Frequency.getFrequency(rtask.frequency);

                return new RecurringTask(name, type, date, startTime, duration, endDate, frequency);
            } else {
                throw new RuntimeException("Error while reading Task");
            }
        }

        private static final class FileTask {
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
        }

    }

    private static class RecurringTaskTypeAdapter extends TaskTypeAdapter{
        private static final TaskTypeAdapter adapter = new RecurringTaskTypeAdapter();

        public static TypeAdapter<? extends Task> get(){
            return adapter;
        }

        @Override
        public void write(final JsonWriter out, final Task value) throws IOException {
            out.beginObject();
            if(value.getClass() == RecurringTask.class){
                final RecurringTask recurring = (RecurringTask) value;

                out.name("Name").value(recurring.getName());
                out.name("Type").value(recurring.getType());
                out.name("StartDate").value(TaskParser.parseDateToInteger(recurring.getStartDate()));
                out.name("StartTime").value(TaskParser.parseTimeToDouble(recurring.getStartTime()));
                out.name("Duration").value(TaskParser.parseDuration(recurring.getDuration()));
                out.name("EndDate").value(TaskParser.parseDateToInteger(recurring.getEndDate()));
                out.name("Frequency").value(recurring.getFrequency().getKey());
            } else {
                out.name("Name").value(value.getName());
                out.name("Type").value(value.getType());
                out.name("Date").value(TaskParser.parseDateToInteger(value.getDate()));
                out.name("StartTime").value(TaskParser.parseTimeToDouble(value.getStartTime()));
                out.name("Duration").value(TaskParser.parseDuration(value.getDuration()));
            }
            out.endObject();
        }
    }

    private static final class TaskExclusionStrategy implements ExclusionStrategy {
        private static final Set<Type> EXCLUDED = Set.of(
                RecurringTask.class,
                AntiTask.class
        );

        private static final TaskExclusionStrategy strategy = new TaskExclusionStrategy();

        public static ExclusionStrategy get(){
            return strategy;
        }

        private TaskExclusionStrategy(){}

        @Override
        public boolean shouldSkipField(final FieldAttributes f) {
            return false;
        }

        @Override
        public boolean shouldSkipClass(final Class<?> clazz) {
            if(clazz == RecurringTransientTask.class){
                return true;
            }

            if(clazz == AntiTask.class){
                return true;
            }

            return false;
        }
    }

}
