package com.edu.cpp.cs.cs3560;

import com.edu.cpp.cs.cs3560.model.tasks.AbstractTask;
import com.edu.cpp.cs.cs3560.model.tasks.Task;
import com.edu.cpp.cs.cs3560.model.tasks.recurring.RecurringTask;
import com.edu.cpp.cs.cs3560.model.tasks.trans.TransientTask;
import com.edu.cpp.cs.cs3560.ui.TextUserInterface;
import com.edu.cpp.cs.cs3560.util.JSONUtils;
import com.edu.cpp.cs.cs3560.util.TaskSerializer;
import com.edu.cpp.cs.cs3560.view.View;

import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws Exception {

        String path = "src/tasks.json";

        String content = new String(Files.readAllBytes(Paths.get(path)));

        TaskSerializer.tasksFromJson(content).forEach(m -> System.out.println(m.toString()));

    }
}
