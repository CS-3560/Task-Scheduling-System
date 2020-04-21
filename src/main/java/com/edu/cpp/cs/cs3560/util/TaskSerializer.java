package com.edu.cpp.cs.cs3560.util;

import com.edu.cpp.cs.cs3560.model.tasks.Task;
import com.edu.cpp.cs.cs3560.model.tasks.recurring.RecurringTask;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class TaskSerializer {

    private static final TaskSerializer serializer = new TaskSerializer();

    private TaskSerializer(){}

    public static TaskSerializer get(){
        return serializer;
    }

    public static String toJSON(Map<String, ?> map){
        return toString(map, "\":\"", "\",\"", "{\"", "\"}");
    }

    public static String toPrettyJSON(Map<String, ?> map){
        return toString(map, "\" : \"", "\",\n\"", "{\n\"", "\"\n}");
    }

    public static String toJSON(Task task){
        return toString(toMap(task), "\":\"", "\",\"", "{\"", "\"}");
    }

    public static String toPrettyJSON(Task task){
        return toString(toMap(task), "\" : \"", "\",\n\"", "{\n\"", "\"\n}");
    }

    public static String toString(Map<String, ?> map){
        return toString(map, "=", ", ", "{", "}");
    }

    public static String toPrettyString(Map<String, ?> map){
        return toString(map, " = ", ",\n", "{\n", "\n}");
    }

    private static String toString(Map<String, ?> map, String separator, String delimiter, String prefix, String suffix){
        return map.keySet().stream()
                .map(key -> key + separator + map.get(key))
                .collect(Collectors.joining(delimiter, prefix, suffix));
    }

    private static Map<String, ?> toMap(Task task){
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("Name", task.getName());
        map.put("Type", task.getType());
        map.put("Date", task.getDate());
        map.put("StartTime", task.getStartTime());
        map.put("Duration", task.getDuration());

        if(task.getClass() == RecurringTask.class){
            map.put("StartDate", map.remove("Date"));
            map.put("EndDate", ((RecurringTask) task).getEndDate());
            map.put("Frequency", ((RecurringTask) task).getFrequency());
        }

        return map;
    }

    public static List<Map<String,String>> tasksFromJson(String json){
        json = json.replaceAll("\\s", "")
                .replace("\n", "")
                .replace("\"", "")
                .replace("[{", "")
                .replace("}]", "");

        List<Map<String,String>> list = new ArrayList<>();
        for(String obj : json.split("[}][,][{]")){
            list.add(toMap(obj));
        }

        return list;
    }

    public static Map<String, String> toMap(String str){
        Map<String, String> json = new LinkedHashMap<>();
        for(String entry : prepareJson(str).split(",")){
            String[] parsed = entry.split(":");

            String key = parsed[0];
            String value = parsed[1];

            json.put(key, value);
        }

        return json;
    }

    public static String prepareJson(String json){
        return json.replaceAll("\\s", "")
                .replace("\n", "")
                .replace("\"", "")
                .replace("{","")
                .replace("}", "");
    }

}
