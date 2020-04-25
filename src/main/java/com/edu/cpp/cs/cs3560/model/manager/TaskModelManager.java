package com.edu.cpp.cs.cs3560.model.manager;

import com.edu.cpp.cs.cs3560.util.TaskParser;
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
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;


import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;


public class TaskModelManager implements TaskManager {
    private static final String TASK_ALREADY_EXISTS_ERROR_MESSAGE_FORMAT = "Task [%s] already exists.";
    private static final String NO_SUCH_TASK_ERROR_MESSAGE_FORMAT = "Task [%s] does not exists.";

    public final Map<String, Task> tasks = new HashMap<>();
    private final Multimap<Task, Task> schedule = HashMultimap.create();


    @Override
    public Task getTask(String name){
        if(!tasks.containsKey(name)){
            throw new TaskManagerException(String.format(NO_SUCH_TASK_ERROR_MESSAGE_FORMAT, name));
        }

        return tasks.get(name);
    }

    @Override
    public List<Task> getTasks(String name){
        //return new ArrayList<>(schedule.get(name));
        return new ArrayList<>();
    }

    public void addTask(Map<String, Object> data){
        addTask(parseTask(data));
    }

    @Override
    public void addTask(Task task){
        try {
            if (task instanceof TransientTask) {
                addTransientTask((TransientTask) task);
            } else if (task instanceof AntiTask) {
                addAntiTask((AntiTask) task);
            } else if (task instanceof RecurringTask) {
                addRecurringTask((RecurringTask) task);
            } else {
                throw new IllegalArgumentException("Invalid Task Type");
            }

            tasks.put(task.getName(), task);
        } catch (RuntimeException e){
            throw new TaskManagerException(String.format("There was a problem adding task [%s]: %s", task.getName(), e.getCause().getMessage()));
        }
    }

    public void addTransientTask(TransientTask task){
        validate(task);
        schedule.put(task, task);
    }

    public void addAntiTask(AntiTask antiTask){
        try {
            TaskValidator.validateTaskMatchingDateTime(
                    antiTask, schedule.values().stream()
                            .filter(t -> t instanceof RecurringTransientTask)
                            .collect(Collectors.toSet())
            );
        } catch (RuntimeException e){
            throw new TaskManagerException(e);
        }

        /*
            AntiTasks only remove instances of Recurring tasks, it does not save the task it replaces
            so when deleting an antitask, it doesn't add the recrurring instance back
         */

        //TODO: Save recurring instance being removed

        schedule.values().removeIf(t -> TaskValidator.getInstance().checkForMatchingDateTime(antiTask, t));
        tasks.put(antiTask.getName(), antiTask);
    }

    public void addRecurringTask(RecurringTask recurring){
        List<Task> recurringTasks = new LinkedList<>();

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

        schedule.putAll(recurring, recurringTasks);
    }



    @Override
    public boolean taskExists(String name){
        return tasks.containsKey(name);
    }

    @Override
    public List<Task> getTasks(){
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Task> getAllTasks(){
        return new ArrayList<>(schedule.values());
    }

    @Override
    public Task removeTask(String name){
        if(!tasks.containsKey(name)){
            throw new TaskManagerException(String.format(NO_SUCH_TASK_ERROR_MESSAGE_FORMAT, name));
        }

        //TODO: Implement Removing AntiTask (Read the Lecture 15 slides)

        schedule.removeAll(tasks.get(name));
        return tasks.remove(name);
    }

    @Override
    public Task removeTask(Task task){

        //TODO: Implement removing tasks according to slides (Read the Lecture 15 slides)

        return null;
    }

    @Override
    public void updateTask(Task task, Map<String, Object> updates){
        //TODO: Implement this

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

    private Task replaceTask(String original, Task replacement){
        Task og = removeTask(original);
        addTask(replacement);

        return og;
    }

    private void validateUniqueness(Task task){
        if(tasks.containsKey(task.getName()) || schedule.containsKey(task)){
            throw new TaskManagerException(String.format(TASK_ALREADY_EXISTS_ERROR_MESSAGE_FORMAT, task.getName()));
        }
    }

    private void validate(Task task){
        try {
            TaskValidator.validateTaskTypeSupported(task);
            TaskValidator.validateNoTaskOverlap(task, schedule.values());
        } catch (RuntimeException e){
            throw new TaskManagerException(e);
        }
    }

    private Task parseTask(Map<String, Object> data){
        return parseTaskData(data.entrySet().stream().collect(Collectors.toMap(Entry::getKey, e -> String.valueOf(e.getValue()))));
    }

    private Task parseTaskData(Map<String, String> data){
        String name = data.get("Name");
        String type = data.get("Type");
        LocalDate date = TaskParser.parseDate(data.get("Date"));
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
            throw new IllegalArgumentException("Invalid Task Type");
        }
    }

}
