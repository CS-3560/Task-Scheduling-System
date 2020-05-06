// =====================================================================================================================
// TaskSchedulingSystem.java
// =====================================================================================================================
/* About:
 *       This class is responsible for creating our initial helper objects to be used throughout the rest of the
 *       project.
 *       This section of code is our main driver and what initially runs at the start of the program.
 * */
// =====================================================================================================================
package com.edu.cpp.cs.cs3560;

// Various import statements
import com.edu.cpp.cs.cs3560.controller.Engine;
import com.edu.cpp.cs.cs3560.controller.TaskSchedulerEngine;
import com.edu.cpp.cs.cs3560.io.TaskFileReader;
import com.edu.cpp.cs.cs3560.io.TaskFileReaderImpl;
import com.edu.cpp.cs.cs3560.io.TaskFileWriter;
import com.edu.cpp.cs.cs3560.io.TaskFileWriterImpl;
import com.edu.cpp.cs.cs3560.util.TaskDeserializer;
import com.edu.cpp.cs.cs3560.util.TaskMapper;
import com.edu.cpp.cs.cs3560.util.TaskSerializer;
import com.edu.cpp.cs.cs3560.util.TaskTypeDeserializer;
import com.edu.cpp.cs.cs3560.util.TaskTypeMapper;
import com.edu.cpp.cs.cs3560.util.TaskTypeSerializer;
import com.edu.cpp.cs.cs3560.model.manager.TaskManager;
import com.edu.cpp.cs.cs3560.model.manager.TaskModelManager;
import com.edu.cpp.cs.cs3560.ui.TextUserInterface;
import com.edu.cpp.cs.cs3560.ui.UserInterface;
import com.edu.cpp.cs.cs3560.view.TaskView;
import com.edu.cpp.cs.cs3560.view.TextTaskView;


public final class TaskSchedulingSystem {

    // Create objects to be used throughout the code
    // and call our scheduling engine (Controller)
    private static void start(){

        // For turning streams into objects or vice versa
        final TaskSerializer serializer = new TaskTypeSerializer();
        final TaskDeserializer deserializer = new TaskTypeDeserializer();

        // For updating a task
        final TaskMapper mapper = new TaskTypeMapper();

        // File IO Objects
        final TaskFileReader reader = new TaskFileReaderImpl(deserializer);
        final TaskFileWriter writer = new TaskFileWriterImpl(serializer);

        final UserInterface ui = new TextUserInterface();

        final TaskView view = new TextTaskView(ui, serializer);


        final TaskManager manager = new TaskModelManager();

        final Engine controller = new TaskSchedulerEngine(manager, view, reader, writer, mapper);
        controller.run();
    }

    public static void main(final String ...args) {
            start();
    }

}
