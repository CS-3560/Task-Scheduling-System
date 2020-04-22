package com.edu.cpp.cs.cs3560.io.tasks;

import com.edu.cpp.cs.cs3560.model.tasks.Task;
import com.edu.cpp.cs.cs3560.model.tasks.anti.AntiTask;
import com.edu.cpp.cs.cs3560.model.tasks.recurring.RecurringTask;
import com.edu.cpp.cs.cs3560.model.tasks.recurring.RecurringTransientTask;
import com.edu.cpp.cs.cs3560.model.tasks.trans.TransientTask;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Collection;

public class TaskTypeSerializer implements TaskSerializer {
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
            .addDeserializationExclusionStrategy(TaskExclusionStrategy.getStrategy())
            .registerTypeAdapter(TransientTask.class, TaskTypeAdapter.getTaskTypeAdapter())
            .registerTypeAdapter(RecurringTransientTask.class, TaskTypeAdapter.getTaskTypeAdapter())
            .create();


    @Override
    public String serialize(Task task) {
        return gson.toJson(task);
    }

    @Override
    public String serialize(Collection<Task> tasks) {
        return gson.toJson(tasks);
    }

    private static class TaskTypeAdapter<T extends Task> extends TypeAdapter<T> {
        private static final TaskTypeAdapter<? extends Task> adapter = new TaskTypeAdapter<>();

        public static TypeAdapter<? extends Task> getTaskTypeAdapter(){
            return adapter;
        }

        @Override
        public void write(JsonWriter out, T value) throws IOException {
            out.beginObject();
            out.name("Name").value(value.getName());
            out.name("Type").value(value.getType());
            out.name("Date").value(TaskParser.parseDateToInteger(value.getDate()));
            out.name("StartName").value(TaskParser.parseTimeToDouble(value.getStartTime()));
            out.name("Duration").value(TaskParser.parseDuration(value.getDuration()));
            out.endObject();
        }

        @Override
        public T read(JsonReader in) throws IOException {
            return null;
        }
    }

    private static class TaskExclusionStrategy implements ExclusionStrategy {
        private static final TaskExclusionStrategy strategy = new TaskExclusionStrategy();

        public static ExclusionStrategy getStrategy(){
            return strategy;
        }

        private TaskExclusionStrategy(){}

        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return false;
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
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
