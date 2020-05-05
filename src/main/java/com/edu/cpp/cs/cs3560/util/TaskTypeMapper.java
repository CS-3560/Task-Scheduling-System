// =====================================================================================================================
// TaskTypeMapper.java
// =====================================================================================================================
/* About:
 *      This class is designed to be used when updating the information of a task.
 *      Takes the Map<String, Object> data as a parameter.
 *
 *      Implements the TaskMapper.java interface.
 * */
// =====================================================================================================================
package com.edu.cpp.cs.cs3560.util;

import com.edu.cpp.cs.cs3560.model.tasks.Task;
import com.edu.cpp.cs.cs3560.model.tasks.recurring.Frequency;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TaskTypeMapper implements TaskMapper {
    private static final String NAME_KEY = "Name";
    private static final String TYPE_KEY = "Type";
    private static final String DATE_KEY = "Date";
    private static final String START_DATE_KEY = "StartDate";
    private static final String START_TIME_KEY = "StartTime";
    private static final String DURATION_KEY = "Duration";
    private static final String END_DATE_KEY = "EndDate";
    private static final String FREQUENCY_KEY = "Frequency";


    @Override
    public Map<String, Object> toMap(final Task task){
        final Map<String, Object> map = getAsMap(task);

        Map<String, Object> data = new LinkedHashMap<>();
        if(map.containsKey("name")){
            data.put("Name", map.get("name"));
        }

        if(map.containsKey("type")){
            data.put("Type", map.get("type"));
        }

        if(map.containsKey("date")){
            data.put("Date", TaskParser.parseDateToInteger((LocalDate) map.get("date")));
        }

        if(map.containsKey("startDate")){
            data.put("StartDate", TaskParser.parseDateToInteger((LocalDate) map.get("startDate")));
        }

        if(map.containsKey("startTime")){
            data.put("StartTime", TaskParser.parseTimeToDouble((LocalTime) map.get("startTime")));
        }

        if(map.containsKey("duration")){
            data.put("Duration", TaskParser.parseDuration((TemporalAmount) map.get("duration")));
        }

        if(map.containsKey("endDate")){
            data.put("EndDate", TaskParser.parseDateToInteger((LocalDate) map.get("endDate")));
        }

        if(map.containsKey("frequency")){
            data.put("Frequency", Frequency.getFrequencyKey((Frequency) map.get("frequency")));
        }


        return data;
    }

    private Map<String, Object> getAsMap(final Task task){
        try {
            final Map<String, Object> data = new LinkedHashMap<>();
            for (final Field field : getFields(task)) {
                field.setAccessible(true);
                data.put(field.getName(), field.get(task));
            }

            return data;
        } catch (IllegalAccessException e){
            return new LinkedHashMap<>();
        }
    }


    private List<Field> getFields(final Task task){
        final List<Field> fields = new ArrayList<>();

        Class<?> clazz = task.getClass();
        while(clazz != Object.class){
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }

        return fields;
    }







}
