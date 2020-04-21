package com.edu.cpp.cs.cs3560.model.manager;

import com.edu.cpp.cs.cs3560.model.tasks.Task;
import com.edu.cpp.cs.cs3560.model.tasks.anti.AntiTask;
import com.edu.cpp.cs.cs3560.model.tasks.recurring.Frequency;
import com.edu.cpp.cs.cs3560.model.tasks.recurring.RecurringTask;
import com.edu.cpp.cs.cs3560.model.tasks.trans.TransientTask;
import com.edu.cpp.cs.cs3560.model.types.TaskTypes;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class TaskModelManager {
    private final Map<String, Task> tasks = new HashMap<>();

    public Task updateTask(Task task, Map<String, String> updated){
        String type = updated.get("Type");
        if(!task.getType().equals(type)){
            task.setType(type);
        }

        String name = updated.get("Name");
        if(!task.getName().equals(name)){
            task.setName(name);
        }

        LocalDate date = LocalDate.parse(updated.get("Date"));
        if(!task.getDate().equals(date)){
            task.setDate(date);
        }

        LocalTime startTime = LocalTime.parse(updated.get("StartTime"));
        if(!task.getStartTime().equals(startTime)){
            task.setStartTime(startTime);
        }

        Duration duration = Duration.parse(updated.get("Duration"));
        if(!task.getDuration().equals(duration)){
            task.setDuration(duration);
        }

        if(task.getClass() == RecurringTask.class){
            LocalDate endDate = LocalDate.parse(updated.get("EndDate"));
            if(!((RecurringTask) task).getEndDate().equals(endDate)){
                ((RecurringTask) task).setEndDate(endDate);
            }

            Frequency frequency = Frequency.getFrequency(Integer.parseInt(updated.get("Frequency")));
            if(((RecurringTask) task).getFrequency() != frequency){
                ((RecurringTask) task).setFrequency(frequency);
            }

        }

        return task;
    }

    public Collection<Task> getTasks(){ return tasks.values(); }

    public Task getTask(String name){
        return tasks.get(name);
    }

    public boolean taskExists(String name){
        return tasks.containsKey(name);
    }

    public Task deleteTask(String name){
        return tasks.remove(name);
    }

    public Task createTask(Map<String, String> info){
        try {
            Task task = createTask(info, info.get("Type"));

            return tasks.put(task.getName(), task);
        } catch (RuntimeException e){
            throw new IllegalArgumentException("Invalid info");
        }
    }

    private Task createTask(Map<String, String> info, String type){
        return createTask(info, Objects.requireNonNull(TaskTypes.getTaskType(type)));
    }

    private Task createTask(Map<String, String> info, Class<?> type){
        if(type == TransientTask.class){
            return createTransientTask(info);
        } else if (type == AntiTask.class){
            return createAntiTask(info);
        } else if (type == RecurringTask.class){
            return createRecurringTask(info);
        } else {
            throw new IllegalArgumentException();
        }
    }

    private RecurringTask createRecurringTask(Map<String, String> info){
        String name = info.get("Name");
        String type = info.get("Type");
        LocalDate startDate = LocalDate.parse(info.get("Date"));
        LocalTime startTime = LocalTime.parse(info.get("StartTime"));
        Duration duration = Duration.parse(info.get("Duration"));
        LocalDate endDate = LocalDate.parse(info.get("EndDate"));
        Frequency frequency = Frequency.getFrequency(Integer.parseInt(info.get("Frequency")));


        return new RecurringTask(name, type, startDate, startTime, duration, endDate, frequency);
    }

    private TransientTask createTransientTask(Map<String, String> info){
        String name = info.get("Name");
        String type = info.get("Type");
        LocalDate date = LocalDate.parse(info.get("Date"));
        LocalTime startTime = LocalTime.parse(info.get("StartTime"));
        Duration duration = Duration.parse(info.get("Duration"));

        return new TransientTask(name, type, date, startTime, duration);
    }

    private AntiTask createAntiTask(Map<String, String> info){
        String name = info.get("Name");
        String type = info.get("Type");
        LocalDate date = LocalDate.parse(info.get("Date"));
        LocalTime startTime = LocalTime.parse(info.get("StartTime"));
        Duration duration = Duration.parse(info.get("Duration"));

        return new AntiTask(name, type, date, startTime, duration);
    }

}
