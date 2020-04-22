package com.edu.cpp.cs.cs3560;

import com.edu.cpp.cs.cs3560.controller.TaskSchedulerEngine;
import com.edu.cpp.cs.cs3560.io.TaskFileReader;
import com.edu.cpp.cs.cs3560.io.TaskFileReaderImpl;
import com.edu.cpp.cs.cs3560.io.TaskFileWriter;
import com.edu.cpp.cs.cs3560.io.TaskFileWriterImpl;
import com.edu.cpp.cs.cs3560.io.tasks.TaskDeserializer;
import com.edu.cpp.cs.cs3560.io.tasks.TaskSerializer;
import com.edu.cpp.cs.cs3560.io.tasks.TaskTypeDeserializer;
import com.edu.cpp.cs.cs3560.io.tasks.TaskTypeSerializer;
import com.edu.cpp.cs.cs3560.model.manager.TaskManager;
import com.edu.cpp.cs.cs3560.model.manager.TaskModelManager;
import com.edu.cpp.cs.cs3560.model.tasks.Task;
import com.edu.cpp.cs.cs3560.ui.TextUserInterface;
import com.edu.cpp.cs.cs3560.ui.UserInterface;
import com.edu.cpp.cs.cs3560.view.TaskView;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class TaskSchedulingSystem {

    public static void main(String ...args) {
        TaskSerializer serializer = new TaskTypeSerializer();
        TaskDeserializer deserializer = new TaskTypeDeserializer();

        TaskFileReader reader = new TaskFileReaderImpl(deserializer);
        TaskFileWriter writer = new TaskFileWriterImpl(serializer);

        UserInterface ui = new TextUserInterface();

        TaskView view = new TaskView(ui, serializer);

        Multimap<String, Task> taskMultimap = ArrayListMultimap.create();

        TaskManager manager = new TaskModelManager(taskMultimap);

        TaskSchedulerEngine controller = new TaskSchedulerEngine(manager, reader, writer, view);
        controller.run();
    }
}
