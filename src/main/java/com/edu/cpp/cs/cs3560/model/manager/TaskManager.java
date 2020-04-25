package com.edu.cpp.cs.cs3560.model.manager;

import com.edu.cpp.cs.cs3560.model.tasks.Task;

import java.util.List;
import java.util.Map;

public interface TaskManager {

    Task getTask(String name);

    List<Task> getTasks(String name);

    void addTask(Map<String, Object> data);

    void addTask(Task task);

    Task removeTask(String name);

    void updateTask(Task task, Map<String, Object> updates);

    boolean taskExists(String name);

    List<Task> getTasks();

    List<Task> getAllTasks();

}
