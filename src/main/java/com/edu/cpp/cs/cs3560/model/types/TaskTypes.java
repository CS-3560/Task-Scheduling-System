package com.edu.cpp.cs.cs3560.model.types;



import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

import java.util.HashSet;
import java.util.Map;

public final class TaskTypes {
    private static final Map<String, Class<?>> supported = new HashMap<>();

    static {
        Arrays.stream(RecurringTasks.values()).forEach(v -> supported.put(v.getType(), v.getClass()));
        Arrays.stream(TransientTasks.values()).forEach(v -> supported.put(v.getType(), v.getClass()));
        Arrays.stream(AntiTasks.values()).forEach(v -> supported.put(v.getType(), v.getClass()));
    }


    private TaskTypes(){}

    public static Collection<Class<?>> getTaskTypes(){
        return supported.values();
    }

    public static Collection<String> getTaskTypeValues(){
        return supported.keySet();
    }

    public static Class<?> getTaskType(String task){
        return supported.get(task);
    }

    public static boolean isSupportedTask(String task){
        return supported.containsKey(task);
    }

    public enum RecurringTasks {

        CLASS("Class"),
        STUDY("Study"),
        SLEEP("Sleep"),
        EXERCISE("Exercise"),
        WORK("Work"),
        MEAL("Meal");

        public final String type;


        RecurringTasks(String type) {
            this.type = type;
        }

        public String getType(){ return type; }

        @Override
        public String toString(){ return getType(); }

    }

    public enum TransientTasks {

        VISIT("Visit"),
        SHOPPING("Shopping"),
        APPOINTMENT("Appointment");

        public final String type;

        TransientTasks(String type) {
            this.type = type;
        }

        public String getType(){ return type; }

        @Override
        public String toString(){ return getType(); }

    }


    public enum AntiTasks {

        CANCELLATION("Cancellation");

        public final String type;

        AntiTasks(String type) {
            this.type = type;
        }

        public String getType(){ return type; }

        @Override
        public String toString(){ return getType(); }

    }


}
