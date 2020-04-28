package com.edu.cpp.cs.cs3560.util;

import com.edu.cpp.cs.cs3560.model.tasks.Task;

import java.util.Map;

@FunctionalInterface
public interface TaskMapper {

    Map<String, Object> toMap(Task task);

}
