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
import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;

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

    private static final Converter<String, String> converter = CaseFormat.LOWER_CAMEL.converterTo(CaseFormat.UPPER_CAMEL);


    @Override
    public Map<String, Object> toMap(final Task task){
        final Map<String, Object> map = getAsMap(task);

        Map<String, Object> data = new LinkedHashMap<>();
        if(map.containsKey(NAME_KEY)){
            data.put(NAME_KEY, map.get(NAME_KEY));
        }

        if(map.containsKey(TYPE_KEY)){
            data.put(TYPE_KEY, map.get(TYPE_KEY));
        }

        if(map.containsKey(DATE_KEY)){
            data.put(DATE_KEY, TaskParser.parseDateToInteger((LocalDate) map.get(DATE_KEY)));
        }

        if(map.containsKey(START_DATE_KEY)){
            data.put(START_DATE_KEY, TaskParser.parseDateToInteger((LocalDate) map.get(START_DATE_KEY)));
        }

        if(map.containsKey(START_TIME_KEY)){
            data.put(START_TIME_KEY, TaskParser.parseTimeToDouble((LocalTime) map.get(START_TIME_KEY)));
        }

        if(map.containsKey(DURATION_KEY)){
            data.put(DURATION_KEY, TaskParser.parseDuration((TemporalAmount) map.get(DURATION_KEY)));
        }

        if(map.containsKey(END_DATE_KEY)){
            data.put(END_DATE_KEY, TaskParser.parseDateToInteger((LocalDate) map.get(END_DATE_KEY)));
        }

        if(map.containsKey(FREQUENCY_KEY)){
            data.put(FREQUENCY_KEY, Frequency.getFrequencyKey((Frequency) map.get(FREQUENCY_KEY)));
        }


        return data;
    }

    private Map<String, Object> getAsMap(final Task task){
        try {
            final Map<String, Object> data = new LinkedHashMap<>();
            for (final Field field : getFields(task)) {
                field.setAccessible(true);
                data.put(converter.convert(field.getName()), field.get(task));
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
