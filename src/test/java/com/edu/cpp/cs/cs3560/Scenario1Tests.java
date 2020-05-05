package com.edu.cpp.cs.cs3560;

import com.edu.cpp.cs.cs3560.controller.Engine;
import com.edu.cpp.cs.cs3560.controller.TaskSchedulerEngine;
import com.edu.cpp.cs.cs3560.io.TaskFileReader;
import com.edu.cpp.cs.cs3560.io.TaskFileReaderImpl;
import com.edu.cpp.cs.cs3560.io.TaskFileWriter;
import com.edu.cpp.cs.cs3560.io.TaskFileWriterImpl;
import com.edu.cpp.cs.cs3560.model.manager.TaskManager;
import com.edu.cpp.cs.cs3560.model.manager.TaskModelManager;
import com.edu.cpp.cs.cs3560.model.tasks.trans.TransientTask;
import com.edu.cpp.cs.cs3560.ui.TextUserInterface;
import com.edu.cpp.cs.cs3560.ui.UserInterface;
import com.edu.cpp.cs.cs3560.util.TaskDeserializer;
import com.edu.cpp.cs.cs3560.util.TaskMapper;
import com.edu.cpp.cs.cs3560.util.TaskSerializer;
import com.edu.cpp.cs.cs3560.util.TaskTypeDeserializer;
import com.edu.cpp.cs.cs3560.util.TaskTypeMapper;
import com.edu.cpp.cs.cs3560.util.TaskTypeSerializer;
import com.edu.cpp.cs.cs3560.view.TaskView;
import com.edu.cpp.cs.cs3560.view.TextTaskView;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Map;

import static java.lang.System.out;
import static org.mockito.Mockito.when;


public class Scenario1Tests {

    /*
    These Tests are not implemented correctly, this is just a rough draft
     */

    private static final String DELETE_TASK_NAME = "Intern Interview";
    private static final String INVALID_TASK_TYPE_MESSAGE = "Invalid Task Type";
    private static final String OVERLAP_ERROR_MESSAGE_1 = "Task [Watch a movie] overlaps with Task [CS3560-Th]";
    private static final String OVERLAP_ERROR_MESSAGE_2 = "Task [Dinner] overlaps with Task [Intern Interview]";

    private static final TransientTask TRANSIENT_TASK_1 = ScenarioTestsUtils.createTransientTask(
            "Intern Interview",
            "Appointment",
            20200427,
            17,
            2.5
    );

    private static final Map<String, Object> TRANSIENT_TASK_1_DATA = Map.of(
            "Name", "Intern Interview",
            "Type", "Appointment",
            "Date", "202004247",
            "StartTime", "17",
            "Duration", "2.5"
    );

    private static final TransientTask TRANSIENT_TASK_2 = ScenarioTestsUtils.createTransientTask(
            "Watch a movie",
            "Movie",
            20200429,
            21.5,
            2
    );

    private static final Map<String, Object> TRANSIENT_TASK_2_DATA = Map.of(
            "Name", "Watch a movie",
            "Type", "Movie",
            "Date", "20200429",
            "StartTime", "21.5",
            "Duration", "2"
    );

    private static final TransientTask TRANSIENT_TASK_3 = ScenarioTestsUtils.createTransientTask(
            "Watch a movie",
            "Visit",
            20200430,
            18.5,
            2
    );

    private static final Map<String, Object> TRANSIENT_TASK_3_DATA = Map.of(
            "Name", "Watch a movie",
            "Type", "Visit",
            "Date", "202004430",
            "StartTime", "18.5",
            "Duration", "2"
    );


    @Mock
    private final UserInterface mui = new TextUserInterface();

    // For turning streams into objects or vice versa
    private final TaskSerializer serializer = new TaskTypeSerializer();
    private final TaskDeserializer deserializer = new TaskTypeDeserializer();

    // For updating a task
    private final TaskMapper mapper = new TaskTypeMapper();

    // File IO Objects
    private final TaskFileReader reader = new TaskFileReaderImpl(deserializer);
    private final TaskFileWriter writer = new TaskFileWriterImpl(serializer);

    private final TaskView view = new TextTaskView(mui, serializer);


    private final TaskManager manager = new TaskModelManager();

    private final Engine controller = new TaskSchedulerEngine(manager, view, reader, writer, mapper);




    @Test
    public void scenario1Tests(){
        controller.run();

        when(mui.getInput()).thenReturn(ScenarioTestConfig.TEST_SET_1);

        String ui = mui.getInput();


        out.println(ui);


    }





}
