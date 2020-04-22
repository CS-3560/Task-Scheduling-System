package com.edu.cpp.cs.cs3560.model.manager;

import com.edu.cpp.cs.cs3560.io.tasks.TaskParser;
import com.edu.cpp.cs.cs3560.model.types.TaskTypes;
import com.edu.cpp.cs.cs3560.model.types.TaskTypes.AntiTasks;
import com.edu.cpp.cs.cs3560.model.types.TaskTypes.RecurringTasks;
import com.edu.cpp.cs.cs3560.model.types.TaskTypes.TransientTasks;
import com.edu.cpp.cs.cs3560.util.TaskValidator;
import com.edu.cpp.cs.cs3560.model.tasks.NonRecurringTask;
import com.edu.cpp.cs.cs3560.model.tasks.Task;
import com.edu.cpp.cs.cs3560.model.tasks.anti.AntiTask;
import com.edu.cpp.cs.cs3560.model.tasks.recurring.Frequency;
import com.edu.cpp.cs.cs3560.model.tasks.recurring.RecurringTask;
import com.edu.cpp.cs.cs3560.model.tasks.recurring.RecurringTransientTask;
import com.edu.cpp.cs.cs3560.model.tasks.trans.TransientTask;
import com.google.common.collect.Multimap;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;


public class TaskModelManager implements TaskManager {
    private final Map<String, Task> cache = new HashMap<>();

    private final Multimap<String, Task> tasks;


    public TaskModelManager(Multimap<String, Task> multimap){
        this.tasks = multimap;
    }

    @Override
    public Task createTask(Map<String, Object> data){
        Task task = parseTask(data.entrySet().stream().collect(Collectors.toMap(Entry::getKey, String::valueOf)));

        addTask(task);

        return task;
    }

    private Task parseTask(Map<String, String> data){
        String name = data.get("Name");
        String type = data.get("Type");
        LocalDate date = TaskParser.parseDate(data.getOrDefault("Date", data.get("StartDate")));
        LocalTime startTime = TaskParser.parseTime(data.get("StartTime"));
        TemporalAmount duration = TaskParser.parseDuration(data.get("Duration"));

        Type ptype = TaskTypes.getTaskType(type);
        if(ptype == TransientTasks.class){
            return new TransientTask(name, type, date, startTime, duration);
        } else if (ptype == AntiTasks.class){
            return new AntiTask(name, type, date, startTime, duration);
        } else if(ptype == RecurringTasks.class){
            LocalDate endDate = TaskParser.parseDate(data.get("EndDate"));
            Frequency frequency = Frequency.getFrequency(Integer.parseInt(data.get("Frequency")));

            return new RecurringTask(name, type, date, startTime, duration, endDate, frequency);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Task getTask(String name){
        return cache.get(name);
    }

    @Override
    public List<Task> getTasks(String name){
        return new ArrayList<>(tasks.get(name));
    }

    @Override
    public void addTask(Task task){
        if(task instanceof TransientTask){
            addTransientTask((TransientTask) task);
        } else if (task instanceof AntiTask){
            addAntiTask((AntiTask) task);
        } else if(task instanceof RecurringTask){
            addRecurringTask((RecurringTask) task);
        } else {
            throw new IllegalArgumentException();
        }

        cache.put(task.getName(), task);
    }

    public void addTransientTask(TransientTask task){
        validate(task);
        tasks.put(task.getName(), task);
    }

    public void addAntiTask(AntiTask task){
        TaskValidator.validateTaskMatchingDateTime(task, tasks.values());
        tasks.values().removeIf(t -> TaskValidator.getInstance().checkForMatchingDateTime(task, t));
    }

    public void addRecurringTask(RecurringTask recurring){
        List<Task> recurringTasks = new ArrayList<>();

        String name = recurring.getName();
        String type = recurring.getType();
        LocalTime startTime = recurring.getStartTime();
        TemporalAmount duration = recurring.getDuration();
        Frequency frequency = recurring.getFrequency();

        LocalDate start = recurring.getStartDate();
        LocalDate end = recurring.getEndDate();

        LocalDate date = LocalDate.from(start);
        while(date.isBefore(end)){
            Task task = new RecurringTransientTask(name, type, date, start, startTime, duration, end, frequency);

            validate(task);

            recurringTasks.add(task);

            date = date.plus(1, frequency.getUnit());
        }

        tasks.putAll(name, recurringTasks);
    }

    @Override
    public boolean taskExists(String name){
        return cache.containsKey(name);
    }

    @Override
    public List<Task> getTasks(){
        return new ArrayList<>(cache.values());
    }

    @Override
    public List<Task> getAllTasks(){
        return new ArrayList<>(tasks.values());
    }

    @Override
    public Task removeTask(String name){
        tasks.removeAll(name);
        return cache.remove(name);
    }

    private Task replaceTask(String original, Task replacement){
        Task og = removeTask(original);
        addTask(replacement);

        return og;
    }

    @Override
    public void updateTask(Task task, Map<String, Object> updates){
        if(updates.containsKey("Type")){
            String updatedType = (String) updates.get("Type");
        }

        if(updates.containsKey("StartTime")){
            LocalTime updatedStartTime = (LocalTime) updates.get("StartTime");
        }

        if(updates.containsKey("Duration")){
            TemporalAmount duration = (TemporalAmount) updates.get("Duration");
        }

        if(task instanceof NonRecurringTask){
            if(updates.containsKey("Date")){
                LocalDate updatedDate = (LocalDate) updates.get("Date");
            }
        }

        if(task instanceof RecurringTask){
            if(updates.containsKey("StartDate")){
                LocalDate updatedStartDate = (LocalDate) updates.get("StartDate");
                ((RecurringTask) task).setStartDate(updatedStartDate);
            }

            if(updates.containsKey("EndDate")){
                LocalDate updatedEndDate = (LocalDate) updates.get("EndDate");
                ((RecurringTask) task).setEndDate(updatedEndDate);
            }
        }

        if(updates.containsKey("Name")){
            String originalName = task.getName();
            String updatedName = (String) updates.get("Name");
            task.setName(updatedName);
            replaceTask(originalName, task);
        }

    }

    private void validate(Task task){
        if(cache.containsKey(task.getName()) || tasks.containsKey(task.getName())){
            throw new RuntimeException("Task Already Exists");
        }

        TaskValidator.validateTaskTypeSupported(task);

        TaskValidator.validateNoTaskOverlap(task, tasks.values());
    }

}
