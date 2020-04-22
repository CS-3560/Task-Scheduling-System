package com.edu.cpp.cs.cs3560.io;

import com.edu.cpp.cs.cs3560.io.tasks.TaskDeserializer;
import com.edu.cpp.cs.cs3560.model.tasks.Task;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class TaskFileReaderImpl implements TaskFileReader {
    private final TaskDeserializer deserializer;

    public TaskFileReaderImpl(TaskDeserializer deserializer) {
        this.deserializer = deserializer;
    }

    @Override
    public List<Task> read(String file) throws IOException {
        return deserializer.deserialize(new String(Files.readAllBytes(Paths.get(file))));
    }

    @Override
    public List<Task> read(File file) throws IOException {
        return deserializer.deserialize(FileUtils.readFileToString(file, "UTF-8"));
    }

}
