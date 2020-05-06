// =====================================================================================================================
// TaskModelManager.java
// =====================================================================================================================
/* About:
 *      This class receives orders from the controller (TaskSchedulerEngine.java) for task manipulation.
 *      Manipulations include creating, viewing, editing, and deleting tasks.
 * */
// =====================================================================================================================
package com.edu.cpp.cs.cs3560.model.manager;

import com.edu.cpp.cs.cs3560.util.TaskParser;
import com.edu.cpp.cs.cs3560.model.types.TaskTypes;
import com.edu.cpp.cs.cs3560.model.types.TaskTypes.AntiTasks;
import com.edu.cpp.cs.cs3560.model.types.TaskTypes.RecurringTasks;
import com.edu.cpp.cs.cs3560.model.types.TaskTypes.TransientTasks;
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
import java.util.Objects;
import java.util.stream.Collectors;


public class TaskModelManager implements TaskManager {
    public final Map<String, Task> tasks = new HashMap<>();
    private final Multimap<Task, Task> schedule = HashMultimap.create();

    public TaskModelManager(){}

    @Override
    public Task getTask(final String name){
        if(!tasks.containsKey(name)){
            throw new TaskManagerException(String.format("Task [%s] does not exists.", name));
        }

        return tasks.get(name);
    }

    @Override
    public List<Task> getTasks(final String name){
        return new ArrayList<>(schedule.get(tasks.get(name)));
    }

    public void addTask(final Map<String, Object> data){
        addTask(parseTask(data));
    }

    @Override
    public void addTask(final Task task){
        if(taskExists(task.getName())){
            throw new TaskManagerException(String.format("Task [%s] already exists", task.getName()));
        }

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
            throw new TaskManagerException(String.format("There was a problem adding task [%s]: %s", task.getName(), e.getMessage()));
        }
    }

    public void addTransientTask(final TransientTask task){
        validate(task);
        schedule.put(task, task);
    }

    public void addAntiTask(final AntiTask antiTask){
        final boolean matching = tasks.values().stream().filter(t -> t.matchInterval(antiTask)).count() == 1;
        if(!matching){
            throw new TaskManagerException("There must be only one instance of recurring task for an anti-task");
        }


        /*
            AntiTasks only remove instances of Recurring tasks, it does not save the task it replaces
            so when deleting an antitask, it doesn't add the recurring instance back
         */

        //TODO: Save recurring instance being removed

        schedule.values().removeIf(t -> t.matchInterval(antiTask));
        tasks.put(antiTask.getName(), antiTask);
    }

    public void addRecurringTask(final RecurringTask recurring){
        final List<Task> recurringTasks = new LinkedList<>();

        final String name = recurring.getName();
        final String type = recurring.getType();
        final LocalTime startTime = recurring.getStartTime();
        final TemporalAmount duration = recurring.getDuration();
        final Frequency frequency = recurring.getFrequency();

        final LocalDate start = recurring.getStartDate();
        final LocalDate end = recurring.getEndDate();

        LocalDate date = LocalDate.from(start);
        while(date.isBefore(end)){
            final Task task = new RecurringTransientTask(name, type, date, start, startTime, duration, end, frequency);

            validate(task);
            recurringTasks.add(task);

            date = date.plus(1, frequency.getUnit());
        }

        schedule.putAll(recurring, recurringTasks);
    }

    @Override
    public void clear() {
        schedule.clear();
        tasks.clear();
    }

    @Override
    public boolean taskExists(final String name){
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
    public Task removeTask(final String name){
        if(!tasks.containsKey(name)){
            throw new TaskManagerException(String.format("Task [%s] does not exists.", name));
        }

        schedule.removeAll(tasks.get(name));
        return tasks.remove(name);
    }

    @Override
    public Task removeTask(final Task task){
        return removeTask(task.getName());
    }

    @Override
    public void updateTask(final Task task, Map<String, Object> updates){
        final Task updated = parseTask(updates);

        if(!Objects.equals(task.getType(), updated.getType())){
            throw new IllegalArgumentException("Changing a Task type is not supported.");
        }

        if(!Objects.equals(task.getName(), updated.getName())){
            if(taskExists(updated.getName())){
                throw new TaskManagerException(String.format("Task [%s] already exists", updated.getName()));
            }
        }

        removeTask(task);
        addTask(updated);
    }

    private void validate(final Task task){
        if(!TaskTypes.getTaskTypeValues().contains(task.getType())){
            throw new TaskManagerException(String.format("Task type [%s] not supported", task.getType()));
        }

        final List<String> overlaps = schedule.values().stream()
                .filter(t -> t.overlap(task))
                .map(Task::getName)
                .collect(Collectors.toList());


        if(!overlaps.isEmpty()){
            throw new TaskManagerException(String.format("Task [%s] overlaps with tasks: %s", task.getName(), overlaps.toString()));
        }

    }

    private Task parseTask(final Map<String, Object> data){
        return parseTaskData(data.entrySet().stream().collect(Collectors.toMap(Entry::getKey, e -> String.valueOf(e.getValue()))));
    }

    private Task parseTaskData(final Map<String, String> data){
        try {
            final String name = data.get("Name");
            final String type = data.get("Type");
            final LocalTime startTime = TaskParser.parseTime(data.get("StartTime"));
            final TemporalAmount duration = TaskParser.parseDuration(data.get("Duration"));

            LocalDate date;
            if (data.containsKey("Date")) {
                date = TaskParser.parseDate(data.get("Date"));
            } else if (data.containsKey("StartDate")) {
                date = TaskParser.parseDate(data.get("StartDate"));
            } else {
                throw new TaskManagerException(String.format("There was an issue parsing the task date for task: [%s]", name));
            }

            final Type ptype = TaskTypes.getTaskType(type);
            if (ptype == TransientTasks.class) {
                return new TransientTask(name, type, date, startTime, duration);
            } else if (ptype == AntiTasks.class) {
                return new AntiTask(name, type, date, startTime, duration);
            } else if (ptype == RecurringTasks.class) {
                LocalDate endDate = TaskParser.parseDate(data.get("EndDate"));
                Frequency frequency = Frequency.getFrequency(Integer.parseInt(data.get("Frequency")));

                return new RecurringTask(name, type, date, startTime, duration, endDate, frequency);
            } else {
                throw new IllegalArgumentException("Invalid Task Type");
            }
        } catch (RuntimeException e){
            throw new TaskManagerException(String.format("There was an error parsing the task: [%s]", e.getMessage()));
        }
    }

}
