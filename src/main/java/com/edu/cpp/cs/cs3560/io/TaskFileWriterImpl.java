package com.edu.cpp.cs.cs3560.io;

import com.edu.cpp.cs.cs3560.util.TaskSerializer;
import com.edu.cpp.cs.cs3560.model.tasks.Task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;

public class TaskFileWriterImpl implements TaskFileWriter {
    private static final String IO_ERROR_MESSAGE_FORMAT = "There was an issue writing to file in path [%s].";

    private final TaskSerializer serializer;

    public TaskFileWriterImpl(TaskSerializer serializer) {
        this.serializer = serializer;
    }

    @Override
    public void write(String file, Task task) throws IOException {
        try {
            Files.write(Paths.get(file), serializer.serialize(task).getBytes());
        } catch (IOException e){
            throw new IOException(String.format(IO_ERROR_MESSAGE_FORMAT, file), e);
        }
    }

    @Override
    public void write(String file, Collection<Task> tasks) throws IOException {
        try {
            Files.write(Paths.get(file), serializer.serialize(tasks).getBytes());
        } catch (IOException e){
            throw new IOException(String.format(IO_ERROR_MESSAGE_FORMAT, file), e);
        }
    }

    @Override
    public void write(File file, Task task) throws IOException {
        try (OutputStream os = new FileOutputStream(file)){
            os.write(serializer.serialize(task).getBytes());
        } catch (IOException e){
            throw new IOException(String.format(IO_ERROR_MESSAGE_FORMAT, file), e);
        }
    }

    @Override
    public void write(File file, Collection<Task> tasks) throws IOException {
        try (Writer writer = new FileWriter(file)){
            writer.write(serializer.serialize(tasks));
        } catch (IOException e){
            throw new IOException(String.format(IO_ERROR_MESSAGE_FORMAT, file), e);
        }
    }

}
