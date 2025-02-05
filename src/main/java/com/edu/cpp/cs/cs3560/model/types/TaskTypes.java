// =====================================================================================================================
// TaskTypes.java
// =====================================================================================================================
/* About:
 *      This class cooperates with TaskModelManager.java create the user's desired task.
 *
 *      Sends the different types of tasks a user can create:
 *      Recurring:          Transient:              AntiTask:
 *          - Class             - Visit                 -Cancellation
 *          - Study             - Shopping
 *          - Sleep             - Appointment
 *          - Exercise
 *          - Work
 *          - Meal
 * */
// =====================================================================================================================
package com.edu.cpp.cs.cs3560.model.types;

import com.edu.cpp.cs.cs3560.model.tasks.anti.AntiTask;
import com.edu.cpp.cs.cs3560.model.tasks.recurring.RecurringTask;
import com.edu.cpp.cs.cs3560.model.tasks.trans.TransientTask;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;


import java.util.List;
import java.util.Map;

public final class TaskTypes {
    private static final Map<String, Type> supported = new HashMap<>();
    private static final Multimap<Type, String> fields = LinkedHashMultimap.create();

    static {
        Arrays.stream(RecurringTasks.values()).forEach(v -> supported.put(v.getType(), v.getClass()));
        Arrays.stream(TransientTasks.values()).forEach(v -> supported.put(v.getType(), v.getClass()));
        Arrays.stream(AntiTasks.values()).forEach(v -> supported.put(v.getType(), v.getClass()));

        List<String> nonrecurring = List.of("Date", "StartTime", "Duration");
        List<String> recurring = List.of("StartDate", "StartTime", "Duration", "EndDate", "Frequency");

        fields.putAll(TransientTasks.class, nonrecurring);
        fields.putAll(AntiTasks.class, nonrecurring);
        fields.putAll(RecurringTasks.class, recurring);
    }


    private TaskTypes(){}

    public static Collection<Type> getTaskTypes(){
        return supported.values();
    }

    public static Collection<String> getTaskTypeValues(){
        return supported.keySet();
    }

    public static Type getTaskType(String task){
        return supported.get(task);
    }

    public static boolean isSupportedTask(String task){
        return supported.containsKey(task);
    }

    public static Collection<String> getFields(String type){
        return getFields(supported.get(type));
    }

    public static Collection<String> getFields(Type type){
        return fields.get(type);
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

        public final String getType(){ return type; }

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

        public final String getType(){ return type; }

        @Override
        public String toString(){ return getType(); }

    }


    public enum AntiTasks {
        CANCELLATION("Cancellation");

        public final String type;

        AntiTasks(String type) {
            this.type = type;
        }

        public final String getType(){ return type; }

        @Override
        public String toString(){ return getType(); }

    }

}
