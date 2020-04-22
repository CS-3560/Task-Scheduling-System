package com.edu.cpp.cs.cs3560.io;

import com.edu.cpp.cs.cs3560.io.tasks.TaskSerializer;
import com.edu.cpp.cs.cs3560.model.tasks.Task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;

public class TaskFileWriterImpl implements TaskFileWriter {
    private final TaskSerializer serializer;

    public TaskFileWriterImpl(TaskSerializer serializer) {
        this.serializer = serializer;
    }

    @Override
    public void write(String file, Task task) throws IOException {
        Files.write(Paths.get(file), serializer.serialize(task).getBytes());
    }

    @Override
    public void write(String file, Collection<Task> tasks) throws IOException {
        Files.write(Paths.get(file), serializer.serialize(tasks).getBytes());
    }

    @Override
    public void write(File file, Task task) throws IOException {
        try (OutputStream os = new FileOutputStream(file)){
            os.write(serializer.serialize(task).getBytes());
        }
    }

    @Override
    public void write(File file, Collection<Task> tasks) throws IOException {
        try (OutputStream os = new FileOutputStream(file)){
            os.write(serializer.serialize(tasks).getBytes());
        }
    }

}
