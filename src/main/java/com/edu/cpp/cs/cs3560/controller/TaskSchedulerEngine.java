// =====================================================================================================================
// TaskSchedulerEngine.java
// =====================================================================================================================
/* About:
 *      The "brain" of our project, this class is designed to be the central hub of our project.
 *
 *      Our Controller calls view to display the user menu and retrieve user input for the controller to make a
 *      decision about which methods to call when.
 * */
// =====================================================================================================================
package com.edu.cpp.cs.cs3560.controller;

import com.edu.cpp.cs.cs3560.io.TaskFileReader;
import com.edu.cpp.cs.cs3560.io.TaskFileWriter;
import com.edu.cpp.cs.cs3560.util.TaskMapper;
import com.edu.cpp.cs.cs3560.util.TaskParser;
import com.edu.cpp.cs.cs3560.model.manager.TaskManager;
import com.edu.cpp.cs.cs3560.model.tasks.Task;
import com.edu.cpp.cs.cs3560.model.types.PSSOperation;
import com.edu.cpp.cs.cs3560.model.types.PSSOperation.PSSOperationType;
import com.edu.cpp.cs.cs3560.view.TaskView;

import java.io.IOException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

// Main Task Scheduling Class implementing the Engine Interface
public class TaskSchedulerEngine implements Engine {
    private final TaskManager manager;
    private final TaskView view;
    private final TaskFileReader reader;
    private final TaskFileWriter writer;
    private final TaskMapper mapper;

    // Constructor
    public TaskSchedulerEngine(final TaskManager manager, final TaskView view, final TaskFileReader reader, final TaskFileWriter writer, final TaskMapper mapper) {
        this.manager = manager;
        this.view = view;
        this.reader = reader;
        this.writer = writer;
        this.mapper = mapper;
    }

    @Override
    public void run() {
        try{
            view.displayMessage("Task Scheduling System:");
            runEngine();
        } catch (Exception e) {
            view.displayError(e.getMessage());
            runEngine();
        }
    }

    // This function calls view to display menu
    // and retrieves user option.
    private void runEngine() {
        PSSOperation operation;
        do {
            operation = view.getOperation();

            final Map<String, Object> data = operation.getData();

            // Based on user input, controller will select the correct function
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
                    view.displayMessage("Invalid Operation. Please Try Again\n");
                    break;
            }
        } while (operation.getType() != PSSOperationType.QUIT);

        shutdown();
    }

    @Override
    public void shutdown(){
        System.exit(0);
    }

    private void createTask(final Map<String, Object> data) {
        try {
            manager.addTask(data);
            view.displayMessage("Task added.");
        } catch (RuntimeException e){
            view.displayError(String.format("There was an error creating the task. %s", e.getMessage()));
        }
    }

    private void viewTask(final Map<String, Object> data) {
        try {
            view.displayTask(manager.getTask(String.valueOf(data.get("Name"))));
        } catch (RuntimeException e){
            view.displayError(String.format("There was an error when viewing the task: %s", e.getMessage()));
        }
    }

    private void deleteTask(final Map<String, Object> data) {
        try {
            view.displayTask(manager.removeTask(String.valueOf(data.get("Name"))));
            view.displayMessage("Task Removed.");
        } catch (RuntimeException e){
            view.displayError(String.format("There was an error when deleting the task: %s", e.getMessage()));
        }
    }

    private void updateTask(final Map<String, Object> data) {
        try {
            final String name = String.valueOf(data.get("Name"));
            if(manager.taskExists(name)){
                final Task original = manager.getTask(name);
                final Map<String, Object> updated = view.getUpdatedInfo(mapper.toMap(original));

                manager.updateTask(original, updated);
            } else {
                view.displayError(String.format("There is no task with name [%s]", name));
            }
        } catch (RuntimeException e){
            view.displayError(String.format("There was a problem updating the task: %s", e.getMessage()));
        }
    }

    private void schedule(final Map<String, Object> data) {
        try {
            final List<Task> schedule = getSchedule(data);

            Collections.sort(schedule);

            final Object operation = data.get("Operation");
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

    private void readFromFile(final Map<String, Object> data) {
        try {
            readFromFile(String.valueOf(data.get("File"))).forEach(manager::addTask);
        } catch (RuntimeException e){
            view.displayError(e.getMessage());
        }
    }

    private List<Task> readFromFile(final String file) {
        try {
            return reader.read(file);
        } catch (IOException e){
            view.displayError(e.getMessage());
        }

        return Collections.emptyList();
    }

    private void writeToFile(final Map<String, Object> data) {
        final List<Task> tasks = manager.getAllTasks();
        Collections.sort(tasks);

        writeToFile(String.valueOf(data.get("File")), tasks);
    }

    private void writeToFile(final String file, final Collection<Task> tasks) {
        try {
            writer.write(file, tasks);
        } catch (IOException e) {
            view.displayError(e.getMessage());
        }
    }

    private List<Task> getSchedule(final Map<String, Object> data) {
        try {
            final Object type = data.get("Type");
            if (type.equals("WHOLE")) {
                return manager.getAllTasks();
            }

            final LocalDateTime start = TaskParser.parseDate(String.valueOf(data.get("StartDate"))).atStartOfDay();
            final LocalDateTime end;
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

    private List<Task> getSchedule(final LocalDateTime start, final LocalDateTime end) {
        final List<Task> schedule = new ArrayList<>();
        for(final Task task : manager.getAllTasks()){
            final LocalDateTime tstart = LocalDateTime.of(task.getDate(), task.getStartTime());
            final LocalDateTime tend = tstart.plus(task.getDuration());

            if(tstart.isBefore(end) && tend.isAfter(start)){
                schedule.add(task);
            }
        }

        return schedule;
    }

}
