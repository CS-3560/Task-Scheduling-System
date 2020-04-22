package com.edu.cpp.cs.cs3560.util;

import com.edu.cpp.cs.cs3560.model.tasks.Task;
import com.edu.cpp.cs.cs3560.model.types.TaskTypes;

import java.time.LocalTime;
import java.util.Collection;

public class TaskValidator {
    private static final TaskValidator validator = new TaskValidator();


    private TaskValidator(){}

    public static TaskValidator getInstance(){
        return validator;
    }

    public static void validateTaskTypeSupported(Task task){
        validator.validateTypeSupported(task);
    }

    public void validateTypeSupported(Task task){
        if(!isTypeSupported(task)) throw new RuntimeException("Invalid Task Type");
    }

    public boolean isTypeSupported(Task task){
        return TaskTypes.getTaskTypeValues().contains(task.getType());
    }

    public static void validateNoTaskOverlap(Task task, Collection<Task> others){
        validator.validateNoOverlap(task, others);
    }

    public void validateNoOverlap(Task task, Collection<Task> others){
        for(Task other : others){
            if(checkForOverlap(task, other)){
                throw new RuntimeException(String.format("Task [%s] overlaps with Task [%s]", task.getName(), other.getName()));
            }
        }
    }

    public boolean checkForOverlap(Task task, Collection<Task> others){
        return others.stream().anyMatch(o -> checkForOverlap(task, o));
    }

    public boolean checkForOverlap(Task task, Task other){
        return (task.getDateTime().isBefore(other.getEndDateTime()) && task.getEndDateTime().isAfter(other.getDateTime()));
    }

    public static void validateTaskMatchingDateTime(Task task, Collection<Task> others){
        validator.validateMatchingDateTime(task, others);
    }

    public void validateMatchingDateTime(Task task, Collection<Task> others){
        for(Task other : others){
            if(checkForMatchingDateTime(task, other)){
                return;
            }
        }

        throw new RuntimeException(String.format("No tasks match with task [%s]", task.getName()));
    }

    public boolean checkForMatchingDateTime(Task task, Collection<Task> others){
        return others.stream().anyMatch(o -> checkForMatchingDateTime(task, o));
    }

    public boolean checkForMatchingDateTime(Task task, Task other){
        return task.getDate().equals(other.getDate())
                && task.getStartTime().equals(other.getStartTime())
                && task.getDuration().equals(other.getDuration());
    }

    public static boolean validateTime(LocalTime time){
        return time.getHour() > 0 && time.getHour() < 23.75 && time.getMinute() % 15 == 0;
    }

    public static boolean validateTime(double time){
        return time >= 0.25 && time <= 23.75 && time % 0.25 == 0;
    }

    public static boolean validateDuration(double duration){
        return duration >= 0.25 && duration <= 23.75 && duration % 0.25 == 0;
    }

}
