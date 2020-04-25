package com.edu.cpp.cs.cs3560.util;

import com.edu.cpp.cs.cs3560.model.tasks.Task;

import java.util.List;

@FunctionalInterface
public interface TaskDeserializer {

    List<Task> deserialize(String json);

}
