package com.edu.cpp.cs.cs3560.controller;

import com.edu.cpp.cs.cs3560.io.TaskFileReader;
import com.edu.cpp.cs.cs3560.io.TaskFileWriter;
import com.edu.cpp.cs.cs3560.util.TaskParser;
import com.edu.cpp.cs.cs3560.model.manager.TaskManager;
import com.edu.cpp.cs.cs3560.model.tasks.Task;
import com.edu.cpp.cs.cs3560.model.types.PSSOperation;
import com.edu.cpp.cs.cs3560.model.types.PSSOperation.PSSOperationType;
import com.edu.cpp.cs.cs3560.view.TaskView;
import com.edu.cpp.cs.cs3560.view.TextTaskView;

import java.io.IOException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TaskSchedulerEngine implements Engine {
    private final TaskManager manager;
    private final TaskFileReader reader;
    private final TaskFileWriter writer;
    private final TaskView view;

    public TaskSchedulerEngine(TaskManager manager, TaskFileReader reader, TaskFileWriter writer, TaskView view) {
        this.manager = manager;
        this.reader = reader;
        this.writer = writer;
        this.view = view;
    }

    @Override
    public void run(){
        try{
            runEngine();
        } catch (Exception e){
            view.displayError(e.getMessage());
            runEngine();
        }
    }

    private void runEngine(){
        PSSOperation operation;
        do{
            operation = view.getOperation();

            Map<String, Object> data = operation.getData();
            switch (operation.getType()){
                case CREATE_TASK:
                    createTask(data);
                    break;
                case VIEW_TASK:
                    viewTask(data);
                    break;
                case DELETE_TASK:
                    deleteTask(data);
                    break;
                case EDIT_TASK:
                    updateTask(data);
                    break;
                case READ_FROM_FILE:
                    readFromFile(data);
                    break;
                case WRITE_TO_FILE:
                    writeToFile(data);
                    break;
                case VIEW_SCHEDULE:
                case WRITE_SCHEDULE:
                    schedule(data);
                    break;
                case QUIT:
                    shutdown();
                    break;
                default:
                    throw new RuntimeException("Error while running");

            }
        } while (operation.getType() != PSSOperationType.QUIT);

        shutdown();
    }

    @Override
    public void shutdown(){
        System.exit(0);
    }

    private void createTask(Map<String, Object> data){
        try {
            manager.addTask(data);
            view.displayMessage("Task added.");
        } catch (RuntimeException e){
            view.displayError(String.format("There was an error creating the task %s", e.getMessage()));
        }
    }

    private void viewTask(Map<String, Object> data){
        try {
            view.displayTask(manager.getTask(String.valueOf(data.get("Name"))));
        } catch (RuntimeException e){
            view.displayError(String.format("There was an error when viewing the task: %s", e.getMessage()));
        }
    }

    private void deleteTask(Map<String, Object> data){
        try {
            view.displayTask(manager.removeTask(String.valueOf(data.get("Name"))));
            view.displayMessage("Task Removed.");
        } catch (RuntimeException e){
            view.displayError(String.format("There was an error when deleting the task: %s", e.getMessage()));
        }
    }

    private void updateTask(Map<String, Object> data){
        //TODO: Implement editing a task
    }

    private void schedule(Map<String, Object> data){
        try {
            List<Task> schedule = getSchedule(data);

            Collections.sort(schedule);

            Object operation = data.get("Operation");
            if (Objects.equals(operation, "VIEW")) {
                view.displayTasks(schedule);
            } else if (Objects.equals(operation, "WRITE")) {
                writeToFile(String.valueOf(data.get("File")), schedule);
            } else {
                throw new RuntimeException(String.format("Invalid operation [%s]", operation.toString()));
            }
        } catch (RuntimeException e){
            view.displayError(String.format("There was a problem with getting the schedule: %s", e.getMessage()));
        }
    }

    private void readFromFile(Map<String, Object> data){
        try {
            readFromFile(String.valueOf(data.get("File"))).forEach(manager::addTask);
        } catch (RuntimeException e){
            view.displayError(e.getMessage());
        }
    }

    private List<Task> readFromFile(String file){
        try {
            return reader.read(file);
        } catch (IOException e){
            view.displayError(e.getMessage());
        }

        return Collections.emptyList();
    }

    private void writeToFile(Map<String, Object> data){
        List<Task> tasks = manager.getAllTasks();
        Collections.sort(tasks);

        writeToFile(String.valueOf(data.get("File")), tasks);
    }

    private void writeToFile(String file, Collection<Task> tasks){
        try {
            writer.write(file, tasks);
        } catch (IOException e) {
            view.displayError(e.getMessage());
        }
    }

    private List<Task> getSchedule(Map<String, Object> data){
        try {
            Object type = data.get("Type");
            if (type.equals("WHOLE")) {
                return manager.getAllTasks();
            }

            LocalDateTime start = TaskParser.parseDate(String.valueOf(data.get("StartDate"))).atStartOfDay();
            LocalDateTime end;
            if (type.equals("DAY")) {
                end = start.plusDays(1);
            } else if (type.equals("WEEK")) {
                end = start.plusWeeks(1);
            } else if (type.equals("MONTH")) {
                end = start.plusMonths(1);
            } else {
                throw new RuntimeException(String.format("Invalid schedule type [%s]", type.toString()));
            }

            return getSchedule(start, end);
        } catch (RuntimeException e){
            throw new RuntimeException(String.format("There was a problem with getting the schedule [%s]", e.getMessage()), e);
        }
    }

    private List<Task> getSchedule(LocalDateTime start, LocalDateTime end){
        List<Task> schedule = new ArrayList<>();
        for(Task task : manager.getAllTasks()){
            LocalDateTime tstart = LocalDateTime.of(task.getDate(), task.getStartTime());
            LocalDateTime tend = tstart.plus(task.getDuration());

            if(tstart.isBefore(end) && tend.isAfter(start)){
                schedule.add(task);
            }

        }

        return schedule;
    }


}
