package com.edu.cpp.cs.cs3560.util;

import com.edu.cpp.cs.cs3560.model.tasks.Task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class TaskDataIOUtils {
    private static final String INVALID_DATA_ERROR_MESSAGE = "File contains Invalid data.";


    private TaskDataIOUtils(){
        throw new UnsupportedOperationException();
    }

    public static void saveTaskData(File file, Collection<Task> tasks) throws IOException {
        try (
                FileOutputStream fos = new FileOutputStream(file);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ){
            oos.writeObject(tasks);
        }
    }

    public static void saveTaskData(String file, Collection<Task> tasks) throws IOException {
        saveTaskData(new File(file), tasks);
    }

    public static void saveTaskData(File file, Task task) throws IOException {
        saveTaskData(file, Collections.singleton(task));
    }

    public static void saveTaskData(String file, Task task) throws IOException {
        saveTaskData(new File(file), task);
    }

    @SuppressWarnings("unchecked")
    public static Collection<Task> loadTaskData(File file) throws IOException {
        final Collection<Task> tasks = Collections.checkedCollection(new ArrayList<>(), Task.class);
        try(
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
        ){
            final Object data = ois.readObject();
            if(data instanceof Task){
                tasks.add((Task) data);
            } else if(data instanceof Collection){
                tasks.addAll((Collection<? extends Task>) data);
            } else {
                printError(INVALID_DATA_ERROR_MESSAGE);
            }
        } catch (ClassNotFoundException e) {
            printError(e.getMessage());
        }

        return tasks;
    }

    private static void printError(String e){
        System.err.println(e);
    }

}
