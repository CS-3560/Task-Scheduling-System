package com.edu.cpp.cs.cs3560;

import com.edu.cpp.cs.cs3560.controller.Engine;
import com.edu.cpp.cs.cs3560.controller.TaskSchedulerEngine;
import com.edu.cpp.cs.cs3560.io.TaskFileReader;
import com.edu.cpp.cs.cs3560.io.TaskFileReaderImpl;
import com.edu.cpp.cs.cs3560.io.TaskFileWriter;
import com.edu.cpp.cs.cs3560.io.TaskFileWriterImpl;
import com.edu.cpp.cs.cs3560.util.TaskDeserializer;
import com.edu.cpp.cs.cs3560.util.TaskSerializer;
import com.edu.cpp.cs.cs3560.util.TaskTypeDeserializer;
import com.edu.cpp.cs.cs3560.util.TaskTypeSerializer;
import com.edu.cpp.cs.cs3560.model.manager.TaskManager;
import com.edu.cpp.cs.cs3560.model.manager.TaskModelManager;
import com.edu.cpp.cs.cs3560.ui.TextUserInterface;
import com.edu.cpp.cs.cs3560.ui.UserInterface;
import com.edu.cpp.cs.cs3560.view.TaskView;
import com.edu.cpp.cs.cs3560.view.TextTaskView;

public final class TaskSchedulingSystem {

    public static void main(String ...args) {
        TaskSerializer serializer = new TaskTypeSerializer();
        TaskDeserializer deserializer = new TaskTypeDeserializer();

        TaskFileReader reader = new TaskFileReaderImpl(deserializer);
        TaskFileWriter writer = new TaskFileWriterImpl(serializer);

        UserInterface ui = new TextUserInterface();

        TaskView view = new TextTaskView(ui, serializer);

        TaskManager manager = new TaskModelManager();

        Engine controller = new TaskSchedulerEngine(manager, reader, writer, view);
        controller.run();
    }
}
